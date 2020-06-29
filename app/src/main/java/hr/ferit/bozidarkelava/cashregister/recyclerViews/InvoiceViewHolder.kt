package hr.ferit.bozidarkelava.cashregister.recyclerViews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.containers.CartItem
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.interfaces.InvoiceButtonClicks
import kotlinx.android.synthetic.main.invoice_item.view.*

class InvoiceViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun populateOnViewHolder(item: CartItem, clicks: InvoiceButtonClicks) {

        itemView.tvInvoiceItemId.text = item.getId()
        itemView.tvInvoiceItemName.text = item.getName()
        itemView.tvInvoiceItemQuantity.text = item.getQuantity()
        itemView.tvInvoiceItemPriceForOne.text = item.getPrice()

        //val quantity: Double = itemView.tvInvoiceItemQuantity.text.toString().toDouble()
        //val pricePerOne: Double = itemView.tvInvoiceItemPriceForOne.text.toString().toDouble()

        //val total: Double = quantity*pricePerOne

        itemView.tvInvoiceItemTotalPrice.text=item.getTotal()

        itemView.btnAddItem.setOnClickListener() {
            clicks.add(item.getId().toInt())
        }

        itemView.btnRemove.setOnClickListener() {
            clicks.eliminate(item.getId().toInt())
        }

        itemView.btnRemoveItem.setOnClickListener() {
            clicks.remove(item.getId().toInt())
        }
    }
}