package hr.ferit.bozidarkelava.cashregister.recyclerViews

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.interfaces.ProductButtonsClicks

class ViewStockRecyclerAdapter (products: MutableList<Product>, clicks:ProductButtonsClicks, context: Context): RecyclerView.Adapter<ViewStockViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()
    private val  clicks: ProductButtonsClicks
    private val context: Context

    init {
        this.products.addAll(products)
        this.clicks=clicks
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewStockViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val holder = ViewStockViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        val size: Int = products.size
        return size
    }

    override fun onBindViewHolder(holder: ViewStockViewHolder, position: Int) {
        val product = products[position]
        holder.populateOnViewHolder(product, clicks)
    }

    fun refresh(product: MutableList<Product>) {
        this.products.clear()
        this.products.addAll(product)
        this.notifyDataSetChanged()
    }
}