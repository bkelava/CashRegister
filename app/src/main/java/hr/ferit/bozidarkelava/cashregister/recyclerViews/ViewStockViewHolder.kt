package hr.ferit.bozidarkelava.cashregister.recyclerViews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.interfaces.productButtonsClicks
import hr.ferit.bozidarkelava.cashregister.miscellaneous.QRManager
import kotlinx.android.synthetic.main.item.view.*

class ViewStockViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun populateOnViewHolder(product: Product, click: productButtonsClicks) {
        itemView.tvType.text = "Type: " + product.type
        itemView.tvName.text = "Name: " + product.productName
        itemView.tvUnit.text = "Unit: " + product.unitMeasure
        itemView.tvQuantity.text= "Quantity: " + product.quantity.toString()
        itemView.tvPrice.text= "Price: " + product.price.toString()

        val qrManager = QRManager()
        itemView.ivQR.setImageBitmap(qrManager.createQR(product.id.toString()))

        itemView.btnDelete.setOnClickListener() {
            click.delete(product.id)
        }

        itemView.btnUpdate.setOnClickListener() {
            click.update(product.id)
        }

        itemView.btnExportQR.setOnClickListener() {
            click.export(product.id)
        }
    }
}