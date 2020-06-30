package hr.ferit.bozidarkelava.cashregister.recyclerViews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.containers.CartItem
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.interfaces.InvoiceButtonClicks
import kotlinx.android.synthetic.main.invoice_item.view.*

class InvoiceViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun populateOnViewHolder(item: CartItem) {

        itemView.tvInvoiceItemId.text = item.getId()
        itemView.tvInvoiceItemName.text = item.getName()
        itemView.tvInvoiceItemQuantity.text = item.getQuantity()
        itemView.tvInvoiceItemPriceForOne.text = item.getPrice()

        itemView.tvInvoiceItemTotalPrice.text=item.getTotal()
    }
}