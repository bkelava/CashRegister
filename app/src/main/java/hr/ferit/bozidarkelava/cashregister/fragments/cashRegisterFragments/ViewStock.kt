package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentViewStockBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.FragmentCommunicator
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.interfaces.productButtonsClicks
import hr.ferit.bozidarkelava.cashregister.recyclerViews.ViewStockRecyclerAdapter
import hr.ferit.bozidarkelava.cashregister.singleton.ItemContainer

class ViewStock : Fragment(), Manager, MVVM {

    private val database = CashRegisterDatabase.getInstance().productDao()

    private lateinit var binding: FragmentViewStockBinding

    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_stock, container, false)
        return view
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
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_view_stock)
        communicator = activity as FragmentCommunicator
    }

    override fun setUpBinding() {
        binding.btnBack.setOnClickListener() {
            openFragment(R.id.frameViewStock, MainMenu())
        }

        val mClicks = createClicks()

        binding.etSearchBar.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //empty
            }
        })

        binding.rvViewStockRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvViewStockRecyclerView.adapter = ViewStockRecyclerAdapter(database.selectAll() as MutableList<Product>,mClicks, this!!.context!!)
    }

    private fun filter(string: String) {
        binding.rvViewStockRecyclerView.adapter = ViewStockRecyclerAdapter(database.filter(string) as MutableList<Product>, createClicks(), this!!.context!!)
        (binding.rvViewStockRecyclerView.adapter as ViewStockRecyclerAdapter).refresh(database.filter(string) as MutableList<Product>)
    }

    private fun createClicks(): productButtonsClicks {
        val mClicks = object : productButtonsClicks {
            override fun delete(position: Int) {
                var product = database.selectId(position)
                database.delete(product)
                (binding.rvViewStockRecyclerView.adapter as ViewStockRecyclerAdapter).refresh(
                    database.selectAll() as MutableList<Product>) //refresh
            }

            override fun update(position: Int) {
                setItem(position)
                communicator.passData(position.toString())
            }
        }
        return mClicks
    }

    private fun setItem(position: Int) {
        ItemContainer.setProductType(database.selectId(position).type)
        ItemContainer.setName(database.selectId(position).productName)
        ItemContainer.setUnit(database.selectId(position).unitMeasure)
        ItemContainer.setQuantity(database.selectId(position).quantity.toString())
        ItemContainer.setPrice(database.selectId(position).price.toString())
    }
}