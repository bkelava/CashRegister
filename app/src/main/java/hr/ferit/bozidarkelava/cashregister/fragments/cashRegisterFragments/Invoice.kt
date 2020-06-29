package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp
import hr.ferit.bozidarkelava.cashregister.containers.CartItem
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentInvoiceBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.managers.SearchDialogManager
import hr.ferit.bozidarkelava.cashregister.containers.ItemContainer
import hr.ferit.bozidarkelava.cashregister.interfaces.InvoiceButtonClicks
import hr.ferit.bozidarkelava.cashregister.recyclerViews.InvoiceRecyclerAdapter
import hr.ferit.bozidarkelava.cashregister.viewModels.InvoiceViewModel
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import java.util.ArrayList

class Invoice : Fragment(), MVVM {

    private val databaseProduct = CashRegisterDatabase.getInstance().productDao()

    private lateinit var cartItemList: MutableList<CartItem>

    private lateinit var viewModel: InvoiceViewModel

    private lateinit var manager: Manager

    private lateinit var binding: FragmentInvoiceBinding

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
    }

    override fun setUpBinding() {
        cartItemList = mutableListOf()
        binding.apply {

            val clicks = createClicks()

            binding.total = viewModel

            binding.btnBack.setOnClickListener {
                manager.openFragment(R.id.frameCashRegister, MainMenu())
            }

            binding.btnAdd.setOnClickListener {
                inputItem()
            }

            binding.btnScan.setOnClickListener() {
                Log.d("ITEM", ItemContainer.getName())
            }
            binding.rvInvoiceItems.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.rvInvoiceItems.adapter = InvoiceRecyclerAdapter(cartItemList, clicks, CashRegisterApp.ApplicationContext)
        }
    }

    private fun createClicks(): InvoiceButtonClicks {
        val mClicks = object : InvoiceButtonClicks {
            override fun add(position: Int) {
                //tbc
            }

            override fun eliminate(position: Int) {
                //tbc
            }

            override fun remove(position: Int) {
                //tbc
            }
        }
        return mClicks
    }


    private fun inputItem() {
        SimpleSearchDialogCompat(
            activity,
            "Search...",
            "item name..",
            null,
            initData(),
            SearchResultListener<Searchable> { baseSearchDialogCompat: BaseSearchDialogCompat<Searchable>, searchable: Searchable, _: Int ->
                ItemContainer.setName(searchable.title)
                processInput()
                baseSearchDialogCompat.dismiss()
            }
        ).show()


    }

    private fun processInput() {
        val product = databaseProduct.selectProductByName(ItemContainer.getName())
        cartItemList.add(CartItem(product.id.toString(), product.productName, "1", product.price.toString(), product.price.toString()))
        binding.rvInvoiceItems.adapter?.notifyDataSetChanged()
    }

    private fun initData(): ArrayList<Searchable>? {
        val list: ArrayList<String> = databaseProduct.selectAllItemNames() as ArrayList<String>
        var items: ArrayList<Searchable> = ArrayList()
        for (i in 0 until list.size) {
            items.add(SearchDialogManager(list[i]))
        }
        return items
    }
}