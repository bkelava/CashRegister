package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.Manifest
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformation
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentViewStockBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.FragmentCommunicator
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.interfaces.ProductButtonsClicks
import hr.ferit.bozidarkelava.cashregister.managers.MyNotificationManager
import hr.ferit.bozidarkelava.cashregister.managers.QRManager
import hr.ferit.bozidarkelava.cashregister.miscellaneous.*
import hr.ferit.bozidarkelava.cashregister.recyclerViews.viewStockRecyclerView.ViewStockRecyclerAdapter
import hr.ferit.bozidarkelava.cashregister.containers.ItemContainer
import java.io.File
import java.io.FileOutputStream

class ViewStock : Fragment(), MVVM {

    private lateinit var manager: Manager

    private val productDatabase = CashRegisterDatabase.getInstance().productDao()
    private val companyDatabase = CashRegisterDatabase.getInstance().companyInformationDao()

    private lateinit var binding: FragmentViewStockBinding

    private lateinit var communicator: FragmentCommunicator

    private val FILE_SHARE_PERMISSION: Int = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_stock, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun setUpFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
        }
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_view_stock)
        communicator = activity as FragmentCommunicator
        manager = activity as Manager
    }

    override fun setUpBinding() {
        binding.apply {
            binding.btnBack.setOnClickListener() {
                manager.openFragment(R.id.frameViewStock, MainMenu())
            }

            val mClicks = createClicks()

            binding.etSearchBar.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    filter(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            binding.rvViewStockRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.rvViewStockRecyclerView.adapter =
                ViewStockRecyclerAdapter(
                    productDatabase.selectAll() as MutableList<Product>,
                    mClicks,
                    CashRegisterApp.ApplicationContext
                )
        }
    }

    private fun filter(string: String) {
        binding.rvViewStockRecyclerView.adapter =
            ViewStockRecyclerAdapter(
                productDatabase.filter(string) as MutableList<Product>,
                createClicks(),
                this!!.context!!
            )
        (binding.rvViewStockRecyclerView.adapter as ViewStockRecyclerAdapter).refresh(productDatabase.filter(string) as MutableList<Product>)
    }

    private fun createClicks(): ProductButtonsClicks {
        val mClicks = object : ProductButtonsClicks {
            override fun delete(position: Int) {
                var product = productDatabase.selectId(position)
                productDatabase.delete(product)
                (binding.rvViewStockRecyclerView.adapter as ViewStockRecyclerAdapter).refresh(
                    productDatabase.selectAll() as MutableList<Product>) //refresh
            }

            override fun update(position: Int) {
                setItem(position)
                communicator.passData(position.toString())
            }

            override fun export(position: Int) {
               if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        exportItemToFile(position)
                    }
                    else {
                        activity?.let { requestPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE, FILE_SHARE_PERMISSION) }
                    }
                }
                else {
                   exportItemToFile(position)
                }
            }
        }
        return mClicks
    }

    private fun exportItemToFile(position: Int) {

        val companyInformation: List<CompanyInformation> = companyDatabase.selectAll()

        //Log.d("STUPID",companyInformation.first().companyAddress)
        val product: Product= productDatabase.selectId(position)
        val qrManager = QRManager()

        var pdfDocument: PdfDocument = PdfDocument()
        var paint: Paint = Paint()
        var pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(1572, 1572, 1).create()
        var page: PdfDocument.Page = pdfDocument.startPage(pageInfo)

        var canvas: Canvas = page.canvas

        paint.color = Color.BLACK
        paint.textSize = 50F
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(companyInformation[0].companyName, 786F, 100F, paint)
        canvas.drawText(companyInformation[0].companyAddress, 786F, 150F, paint)
        canvas.drawText(companyInformation[0].companyCityAndPostal, 786F, 200F, paint)

        paint.textSize = 35F
        paint.textAlign=Paint.Align.LEFT

        canvas.drawText("Product name: " + product.productName, 286F, 500F, paint)
        canvas.drawText("Product price: " + product.price.toString(), 286F, 550F, paint)

        canvas.drawBitmap(qrManager.createQR(product.id.toString()), 576F, 576F, paint)

        pdfDocument.finishPage(page)

        
        val path = Environment.getExternalStorageDirectory().absolutePath + File.separator + (product.productName + ".pdf")
        val file = File(path)

        pdfDocument.writeTo(FileOutputStream(file))

        val notificationManager = MyNotificationManager()
        notificationManager.displayNotification(CashRegisterApp.toString(), product.productName, file.absolutePath)

        pdfDocument.close()
    }

    private fun setItem(position: Int) {
        ItemContainer.setProductType(productDatabase.selectId(position).type)
        ItemContainer.setName(productDatabase.selectId(position).productName)
        ItemContainer.setUnit(productDatabase.selectId(position).unitMeasure)
        ItemContainer.setQuantity(productDatabase.selectId(position).quantity.toString())
        ItemContainer.setPrice(productDatabase.selectId(position).price.toString())
    }
}