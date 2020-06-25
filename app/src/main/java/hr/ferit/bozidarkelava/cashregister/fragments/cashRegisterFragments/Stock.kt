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
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentStockBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.singleton.ItemContainer
import hr.ferit.bozidarkelava.cashregister.viewModels.StockViewModel

class Stock : Fragment(), Manager, MVVM {

    private lateinit var binding: FragmentStockBinding
    private lateinit var viewModel: StockViewModel

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

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(layoutID, fragment)
            .commit()
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
    }

    override fun setUpBinding() {
        binding.apply {
            binding.product= viewModel

            binding.btnBack.setOnClickListener() {
                openFragment(R.id.frameStock, MainMenu())
            }

            binding.btnChoose.setOnClickListener() {
                createDialog()
            }

            binding.btnReset.setOnClickListener() {
                resetFields()
            }

            binding.btnSaveProductOrService.setOnClickListener() {
                //TBC
            }
        }
    }

    private fun resetFields() {
        binding.etProductOrServiceName.setText("")
        binding.etProductOrServiceUnitMeasure.setText("")
        binding.etProductQuantity.setText("")
        binding.etPrice.setText("")
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
            0,
            DialogInterface.OnClickListener() { dialog: DialogInterface, which: Int ->
                ItemContainer.setProductType(items[which].toString())
                viewModel.setProductType(ItemContainer.getProductType())
                enableFields()
                dialog.dismiss()
            })
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        Log.d("CONTAINER VALUE", ItemContainer.getProductType())
    }
}