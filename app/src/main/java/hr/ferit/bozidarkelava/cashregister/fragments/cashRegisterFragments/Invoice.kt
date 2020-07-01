package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp
import hr.ferit.bozidarkelava.cashregister.containers.CartItem
import hr.ferit.bozidarkelava.cashregister.containers.ItemContainer
import hr.ferit.bozidarkelava.cashregister.containers.ReceiptDataContainer
import hr.ferit.bozidarkelava.cashregister.containers.TotalPriceContainer
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentInvoiceBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.InvoiceButtonClicks
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.managers.MyNotificationManager
import hr.ferit.bozidarkelava.cashregister.managers.SearchDialogManager
import hr.ferit.bozidarkelava.cashregister.managers.SoundPoolManager
import hr.ferit.bozidarkelava.cashregister.miscellaneous.PDFHelper
import hr.ferit.bozidarkelava.cashregister.miscellaneous.checkPermission
import hr.ferit.bozidarkelava.cashregister.miscellaneous.requestPermission
import hr.ferit.bozidarkelava.cashregister.recyclerViews.invoiceRecyclerView.InvoiceRecyclerAdapter
import hr.ferit.bozidarkelava.cashregister.viewModels.InvoiceViewModel
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import kotlinx.android.synthetic.main.invoice_item.view.*
import java.io.File
import java.util.*

class Invoice : Fragment(), MVVM {

    private val CAMERA_PERMISSION = 105
    private val FILE_SHARE_PERMISSION = 106

    private var quantity: String = "1"
    private var qrScanResult: String = ""

    private val soundManager: SoundPoolManager = SoundPoolManager()

    private val databaseProduct = CashRegisterDatabase.getInstance().productDao()
    private val databaseReceipts = CashRegisterDatabase.getInstance().receiptsDao()

    private var totalPrice: Double = 0.0

    private var itemsMemo: List<Product> = databaseProduct.selectAll()

    private lateinit var cartItemList: MutableList<CartItem>
    private lateinit var productList: ArrayList<Searchable>

    private lateinit var viewModel: InvoiceViewModel
    private lateinit var manager: Manager
    private lateinit var binding: FragmentInvoiceBinding

    private lateinit var adapter: InvoiceRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_invoice, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun setUpFragment() {
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_invoice)
        viewModel = ViewModelProviders.of(this).get(InvoiceViewModel::class.java)
        manager = activity as Manager
        productList = this!!.initData()!!

        viewModel.totalPrice.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })

        soundManager.init()
    }

    override fun setUpBinding() {
        cartItemList = mutableListOf()
        binding.apply {

            val clicks = createClicks()

            binding.total = viewModel

            binding.btnBack.setOnClickListener {
                returnItemState()
                manager.openFragment(R.id.frameCashRegister, MainMenu())
            }

            binding.btnAdd.setOnClickListener {
                inputItem()
            }

            binding.btnScan.setOnClickListener() {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission(Manifest.permission.CAMERA)) {
                        scanQr()
                    }
                    else {
                        activity?.let { requestPermission(it, Manifest.permission.CAMERA, CAMERA_PERMISSION) }
                    }
                }
                else {
                    scanQr()
                }
            }

            binding.btnCreateInvoice.setOnClickListener() {
                if (cartItemList.size == 0) {
                    Toast.makeText(context, "CART IS EMPTY!", Toast.LENGTH_LONG).show()
                }
                else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            createRecepit()
                            soundManager.playSound(R.raw.cash_register)
                        } else {
                            activity?.let {
                                requestPermission(
                                    it,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    FILE_SHARE_PERMISSION
                                )
                            }
                        }
                    } else {
                        createRecepit()
                        soundManager.playSound(R.raw.cash_register)
                    }
                }
            }

            binding.rvInvoiceItems.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter =
                InvoiceRecyclerAdapter(
                    cartItemList,
                    clicks,
                    CashRegisterApp.ApplicationContext
                )
            binding.rvInvoiceItems.adapter = adapter
        }
    }

    private fun createRecepit() {


        //val bitmap = getBitmapFromView(binding.rvInvoiceItems)
        //val bitmap = getScreenshotFromRecyclerView(binding.rvInvoiceItems)
        val bitmap = shotRecyclerView(binding.rvInvoiceItems)
        val root: String = Environment.getExternalStorageDirectory().toString()
        //val bitmap = getRecyclerViewScreenshot(binding.rvInvoiceItems)
        val myDir = File("$root/saved_recipes")
        val pdfHelper = PDFHelper(myDir, this.context!!)
        ReceiptDataContainer.setId(databaseReceipts.selectMaxId() + 1)
        Log.d("AFTER", ReceiptDataContainer.getId().toString())
        pdfHelper.saveImageToPDF(binding.rvInvoiceItems, bitmap!!, ReceiptDataContainer.getId().toString())
        //var list = adapter.getItemList()
        val notificationManager: MyNotificationManager = MyNotificationManager()
        notificationManager.displayNotification("Recipe created!", "Receipit NO"+databaseReceipts.selectLastRecipeNumber().toString(), databaseReceipts.selectLastRecipePath())
        manager.openFragment(R.id.frameCashRegister, MainMenu())
    }

    fun shotRecyclerView(view: RecyclerView): Bitmap? {
        val adapter = view.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            val cacheSize = maxMemory / 8
            val bitmaCache =
                LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                val holder =
                    adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        view.width,
                        View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                    )
                )
                holder.itemView.layout(
                    0, 0, holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmaCache.put(i.toString(), drawingCache)
                }
                height += holder.itemView.measuredHeight
            }
            bigBitmap =
                Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)

            for (i in 0 until size) {
                val bitmap = bitmaCache[i.toString()]
                bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
                iHeight += bitmap.height
                bitmap.recycle()
            }
        }
        return bigBitmap
    }

    private fun scanQr() {
        var intentIntegrator = IntentIntegrator.forSupportFragment(this)
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            .setPrompt("Scan")
            .setCameraId(0)
            .setBeepEnabled(false)
            .setBarcodeImageEnabled(false)
            .initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResultListener: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResultListener != null) {
            if (intentResultListener.contents== null) {
                Toast.makeText(context, "SCAN CANCELLED", Toast.LENGTH_LONG).show()
            }
            else {
                qrScanResult = intentResultListener.contents.toString()
                insertToCartByScan()
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertToCartByScan() {
        if (databaseProduct.selectId(qrScanResult.toInt()) == null) {
            Toast.makeText(context, "ITEM NOT FOUND", Toast.LENGTH_LONG).show()
        }
        else {
            val item = SearchDialogManager(databaseProduct.selectItemNameById(qrScanResult.toInt()))
            val temp: Int = findListElement(item)
            //Log.d("ITEM NAME", item_view_stock.title)
            if (temp < 0) {
                Toast.makeText(context, "ITEM IS ALREADY IN CART", Toast.LENGTH_LONG).show()
            }
            else {
                val product = databaseProduct.selectId(qrScanResult.toInt())
                cartItemList.add(CartItem(product.id.toString(), product.productName, "1", product.price.toString(), product.price.toString()))
                binding.rvInvoiceItems.adapter?.notifyDataSetChanged()

                productList
                val item: SearchDialogManager = SearchDialogManager(product.productName)
                var itemIndex: Int = 0
                itemIndex = findListElement(item)

                productList.removeAt(itemIndex)
                updateQuantityWithOutTotalPrice(cartItemList[cartItemList.size-1])
            }
        }
    }

    private fun findListElement(item: SearchDialogManager) : Int {
        var state: Int = -1
        //Log.d("PRODUCT SIZE", productList.size.toString())
        productList.forEachIndexed { index, searchable ->
            if (searchable.title == item.title)
                state = index
        }
        return state
    }

    private fun returnItemState() {
        for (x in 0 until itemsMemo.size) {
            databaseProduct.update(itemsMemo[x])
        }
    }

    private fun createClicks(): InvoiceButtonClicks {
        val mClicks = object : InvoiceButtonClicks {
            override fun add(position: Int) {
                val item: CartItem = cartItemList[position]
                val product: Product = databaseProduct.selectId(item.getId().toInt())
                if (product.type == "Service") {
                    updateQuantityOnServiceAdding(item, position)
                }
                else {
                    updateQuantityOnProductAdding(item, position)
                }
            }

            override fun eliminate(position: Int) {
                val item: CartItem = cartItemList[position]
                val product: Product = databaseProduct.selectId(item.getId().toInt())
                if (product.type == "Service") {
                    updateQuantityOnServiceEliminating(item, position)
                }
                else {
                    updateQuantityOnProductEliminating(item, position)
                }
            }
            override fun remove(position: Int) {
                val item: CartItem = cartItemList[position]
                val product: Product = databaseProduct.selectId(item.getId().toInt())
                quantity = item.getQuantity()
                if (product.type == "Service") {
                    if(quantity.toInt() == 1) {
                        removeFromTotal(item.getPrice().toDouble())
                        cartItemList.removeAt(position)
                        binding.rvInvoiceItems.adapter?.notifyDataSetChanged()

                        productList.add(SearchDialogManager(item.getName()))
                    }
                    else {
                        Toast.makeText(context,"SET ITEM QUANTITY TO 1", Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    if (quantity.toInt() == 1) {
                        updateQuantityOnProductRemoving(item)
                        cartItemList.removeAt(position)
                        binding.rvInvoiceItems.adapter?.notifyDataSetChanged()

                        productList.add(SearchDialogManager(item.getName()))
                    }
                    else {
                        Toast.makeText(context,"SET ITEM QUANTITY TO 1", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun setText(position: Int): String {
                return quantity
            }
        }
        return mClicks
    }

    private fun updateQuantityOnServiceEliminating(item: CartItem, position: Int) {
        if (item.getQuantity().toInt() > 1) {
            val quantity = item.getQuantity().toInt()-1
            item.setQuantity(quantity.toString())
            this.quantity = item.getQuantity()
            removeFromTotal(item.getPrice().toDouble())
        }
        else {
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isEnabled=false
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isClickable=false
        }
    }

    private fun updateQuantityOnServiceAdding(item: CartItem, position: Int) {
        val quantity = item.getQuantity().toInt() + 1
        item.setQuantity(quantity.toString())
        this.quantity = item.getQuantity()
        addToTotal(item.getPrice().toDouble())
        binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isEnabled=true
        binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isClickable=true
    }
    
    private fun updateQuantityOnProductRemoving(item: CartItem) {
        var quantity = databaseProduct.selectProductQuantity(item.getId().toInt())
        quantity = quantity + 1
        removeFromTotal(item.getPrice().toDouble())
        databaseProduct.updateProductQuantity(item.getId().toInt(), quantity)
    }

    private fun updateQuantityOnProductEliminating(item: CartItem, position: Int) {
        val temp = databaseProduct.selectProductQuantity(item.getId().toInt())
        //Log.d("TEMP BEFORE ELIMINATING", temp.toString())
        if (item.getQuantity().toInt() > 1) {
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnAddItem!!.isEnabled=true
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnAddItem!!.isClickable=true
            var quantity = temp + 1

            databaseProduct.updateProductQuantity(item.getId().toInt(), quantity)
            removeFromTotal(item.getPrice().toDouble())

            quantity = item.getQuantity().toInt()-1
            item.setQuantity(quantity.toString())
            this.quantity=item.getQuantity()

            //Log.d("QUANTITY UPON ELIMINATING: ", quantity.toString())
            this.quantity = quantity.toString()

        }
        else if (item.getQuantity().toInt() == 1)
        {
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isEnabled=false
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isClickable=false
        }
    }

    private fun updateQuantityOnProductAdding(item: CartItem, position: Int) {
        val temp = databaseProduct.selectProductQuantity(item.getId().toInt())
        binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isEnabled=true
        binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnRemove!!.isClickable=true
        if(temp > 0) {
            var quantity = temp - 1

            databaseProduct.updateProductQuantity(item.getId().toInt(), quantity)
            addToTotal(item.getPrice().toDouble())

            quantity = item.getQuantity().toInt() + 1
            item.setQuantity(quantity.toString())
            this.quantity = item.getQuantity()
            //Log.d("QUANTITY UPON ADDING: ", quantity.toString())
        }
        else {
            Toast.makeText(this.requireContext(), "NO ITEM LEFT", Toast.LENGTH_LONG)
        }
        //Log.d("TEMP", temp.toString())
        if (temp == 0) {
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnAddItem!!.isEnabled=false
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnAddItem!!.isClickable=false
        }
        else {
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnAddItem!!.isEnabled=true
            binding.rvInvoiceItems.findViewHolderForAdapterPosition(position)?.itemView?.btnAddItem!!.isClickable=true
        }
    }

    private fun updateQuantityWithOutTotalPrice(item: CartItem) {
        val product: Product = databaseProduct.selectId(item.getId().toInt())
        if (product.type == "Product") {
            if (databaseProduct.selectProductQuantity(item.getId().toInt()) > 0) {
                val quantity: Int = databaseProduct.selectProductQuantity(item.getId().toInt()) - 1
                databaseProduct.updateProductQuantity(item.getId().toInt(), quantity)
                addToTotal(item.getPrice().toDouble())
            } else {
                Toast.makeText(
                    CashRegisterApp.ApplicationContext,
                    "NO ITEM LEFT",
                    Toast.LENGTH_LONG
                )
            }
        }
        else {
            addToTotal(item.getPrice().toDouble())
        }
    }

    private fun inputItem() {

        SimpleSearchDialogCompat(activity, "Search...", "item_view_stock name..", null, productList,
            SearchResultListener { dialog, item, position ->
                ItemContainer.setName(item.title)
                processInput()
                productList.removeAt(position)
                dialog.dismiss()
            }
        ).show()
    }

    private fun processInput() {
        val product = databaseProduct.selectProductByName(ItemContainer.getName())
        cartItemList.add(CartItem(product.id.toString(), product.productName, "1", product.price.toString(), product.price.toString()))
        binding.rvInvoiceItems.adapter?.notifyDataSetChanged()

        updateQuantityWithOutTotalPrice(cartItemList[cartItemList.size-1])
    }

    private fun removeFromTotal(total: Double) {
        this.totalPrice = this.totalPrice - total
        setTotalPriceText()
    }

    private fun addToTotal(total: Double) {
        this.totalPrice = this.totalPrice + total
        setTotalPriceText()
    }

    private fun setTotalPriceText() {
        val value: Double = Math.round(this.totalPrice*100)/100.0
        TotalPriceContainer.setTotalPrice(value)
        viewModel.setTotalPrice(value)
    }

    private fun initData(): ArrayList<Searchable>? {
        val list: ArrayList<String> = databaseProduct.selectAllItemNames() as ArrayList<String>
        val fullList: List<Product> = databaseProduct.selectAll()
        var items: ArrayList<Searchable> = ArrayList()
        for (i in 0 until list.size) {
            if (fullList[i].quantity == 0) {
                //skip
            }
            else {
                items.add(SearchDialogManager(list[i]))
            }
        }
        return items
    }

    override fun onDestroy() {
        super.onDestroy()
        returnItemState()
    }
}