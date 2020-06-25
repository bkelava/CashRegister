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
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.interfaces.productButtonsClicks
import hr.ferit.bozidarkelava.cashregister.recyclerViews.ViewStockRecyclerAdapter

class ViewStock : Fragment(), Manager, MVVM {

    private val database = CashRegisterDatabase.getInstance().productDao()

    private lateinit var binding: FragmentViewStockBinding

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
    }

    override fun setUpBinding() {
        binding.btnBack.setOnClickListener() {
            openFragment(R.id.frameViewStock, MainMenu())
        }

        val mClicks = object: productButtonsClicks {
            override fun delete(position: Int) {
                var product = database.selectId(position)
                database.delete(product)
                (binding.rvViewStockRecyclerView.adapter as ViewStockRecyclerAdapter).refresh(database.selectAll() as MutableList<Product>) //refresh
            }

            override fun update(position: Int) {
                //tbc
            }
        }

        binding.etSearchBar.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //tbc
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //tbc
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //tbc//tbc//tbc//tbc//tbc//tbc
            }
        })

        binding.rvViewStockRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvViewStockRecyclerView.adapter = ViewStockRecyclerAdapter(database.selectAll() as MutableList<Product>,mClicks, this!!.context!!)
    }
}