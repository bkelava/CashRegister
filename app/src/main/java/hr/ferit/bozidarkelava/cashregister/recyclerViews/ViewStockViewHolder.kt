package hr.ferit.bozidarkelava.cashregister.recyclerViews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.interfaces.productButtonsClicks
import kotlinx.android.synthetic.main.item.view.*

class ViewStockViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun populateOnViewHolder(product: Product, click: productButtonsClicks) {
        itemView.tvType.text = product.type
        itemView.tvName.text = product.productName
        itemView.tvUnit.text = product.unitMeasure
        itemView.tvQuantity.text=product.quantity.toString()
        itemView.tvPrice.text=product.price.toString()

        itemView.btnDelete.setOnClickListener() {
            click.delete(product.id)
        }

        itemView.btnUpdate.setOnClickListener() {
            click.update(product.id)
        }
    }

}