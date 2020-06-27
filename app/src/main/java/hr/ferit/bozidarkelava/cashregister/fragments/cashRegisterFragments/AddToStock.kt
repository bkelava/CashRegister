package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentStockBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.singleton.ItemContainer
import hr.ferit.bozidarkelava.cashregister.viewModels.StockViewModel
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

open class AddToStock : Fragment(), MVVM {

    val productDao = CashRegisterDatabase.getInstance().productDao()

    lateinit var binding: FragmentStockBinding
    lateinit var viewModel: StockViewModel

    lateinit var manager: Manager

    private val items: Array<CharSequence> = arrayOf("Product", "Service")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFragment()
    }

    override fun setUpFragment() {
        setUpUI()
        setUpBinding()
        disableFields()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_stock)
        viewModel = ViewModelProviders.of(this).get(StockViewModel::class.java)

        viewModel.productType.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })
        viewModel.notification.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })

        manager = activity as Manager
    }

    override fun setUpBinding() {
        binding.apply {
            binding.product= viewModel
            viewModel.notification.value="Press choose!"
            binding.btnBack.setOnClickListener() {
                manager.openFragment(R.id.frameStock, MainMenu())
            }

            binding.btnChoose.setOnClickListener() {
                createDialog()
                viewModel.notification.value="Fill the rest fields!"
            }

            binding.btnReset.setOnClickListener() {
                resetFields()
            }

            binding.btnSaveProductOrService.setOnClickListener() {
                saveProduct()
            }
        }
    }

    open fun saveProduct() {
        if (binding.etProductOrServiceName.text.toString() == "" ||
            binding.etProductOrServiceUnitMeasure.text.toString() == "" ||
            binding.etProductQuantity.text.toString() == "" ||
            binding.etPrice.text.toString() == "" ) {
            viewModel.notification.value = "FILL THE REQUIRED FIELDS!"
        }
        else {
            val type: String = ItemContainer.getProductType()
            val name = binding.etProductOrServiceName.text.toString()
            val unit = binding.etProductOrServiceUnitMeasure.text.toString()
            val quantity: Int = parseInt(binding.etProductQuantity.text.toString())
            val price: Double = parseDouble(binding.etPrice.text.toString())

            val id: Int = productDao.seletMaxId() + 1

            val product: Product = Product(id, type, name, unit, quantity, price)
            productDao.insert(product)
            resetFields()
        }
    }

    private fun resetFields() {
        binding.etProductOrServiceName.setText("")
        binding.etProductOrServiceUnitMeasure.setText("")
        binding.etProductQuantity.setText("")
        binding.etPrice.setText("")
        viewModel.setProductType("")
        disableFields()
    }

    private fun enableFields() {
        binding.etProductOrServiceName.isEnabled=true
        binding.etProductOrServiceUnitMeasure.isEnabled=true
        if (ItemContainer.getProductType() == "Product") {
            binding.etProductQuantity.isEnabled=true
        }
        else {
            binding.etProductQuantity.setText("1")
            binding.etProductQuantity.isEnabled=false
        }
        binding.etPrice.isEnabled=true
    }

    private fun disableFields() {
        binding.etProductOrServiceName.isEnabled=false
        binding.etProductOrServiceUnitMeasure.isEnabled=false
        binding.etProductQuantity.isEnabled=false
        binding.etPrice.isEnabled=false
    }

    private fun createDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        alertDialogBuilder.setTitle("Select type")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setSingleChoiceItems(
            items,
            -1,
            DialogInterface.OnClickListener() { dialog: DialogInterface, which: Int ->
                ItemContainer.setProductType(items[which].toString())
                viewModel.setProductType(ItemContainer.getProductType())
                enableFields()
                dialog.dismiss()
            })
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}