package hr.ferit.bozidarkelava.cashregister.recyclerViews.invoiceRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.containers.CartItem
import hr.ferit.bozidarkelava.cashregister.interfaces.InvoiceButtonClicks
import kotlinx.android.synthetic.main.invoice_item.view.*

class InvoiceRecyclerAdapter(items: MutableList<CartItem>, clicks: InvoiceButtonClicks, context: Context):
    RecyclerView.Adapter<InvoiceViewHolder>() {

    private var items: MutableList<CartItem>
    private val clicks: InvoiceButtonClicks
    private val context: Context

    init {
        this.items=items
        this.clicks=clicks
        this.context=context
    }

    fun getItemList(): MutableList<CartItem> {
        return this.items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invoice_item, parent, false)
        val holder =
            InvoiceViewHolder(
                view
            )
        return holder
    }

    override fun getItemCount(): Int {
        val size = items.size
        return size
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val item: CartItem = items[position]
        holder.populateOnViewHolder(item)

        holder.itemView.btnRemoveItem.setOnClickListener() {
            clicks.remove(position)
            holder.itemView.tvInvoiceItemQuantity.text = clicks.setText(position)
            notifyItemChanged(position)
        }

        holder.itemView.btnAddItem.setOnClickListener() {
            clicks.add(position)
            holder.itemView.tvInvoiceItemQuantity.text = clicks.setText(position)
            notifyItemChanged(position)
        }

        holder.itemView.btnRemove.setOnClickListener() {
            clicks.eliminate(position)
            holder.itemView.tvInvoiceItemQuantity.text = clicks.setText(position)
            notifyItemChanged(position)
        }
    }
}