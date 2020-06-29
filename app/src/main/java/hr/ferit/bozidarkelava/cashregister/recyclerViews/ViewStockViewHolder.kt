package hr.ferit.bozidarkelava.cashregister.recyclerViews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.interfaces.ProductButtonsClicks
import hr.ferit.bozidarkelava.cashregister.managers.QRManager
import hr.ferit.bozidarkelava.cashregister.managers.SoundPoolManager
import kotlinx.android.synthetic.main.item.view.*

class ViewStockViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val soundPoolManager = SoundPoolManager()

    fun populateOnViewHolder(product: Product, click: ProductButtonsClicks) {

        soundPoolManager.init()

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

        itemView.btnExport.setOnClickListener() {
            click.export(product.id)
            soundPoolManager.playSound(R.raw.juntos)
        }
    }
}