package hr.ferit.bozidarkelava.cashregister.recyclerViews.viewReceiptsRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipts
import kotlinx.android.synthetic.main.item_view_receipts.view.*

class ViewReceiptsViewHolder(view: View): RecyclerView.ViewHolder(view)  {

    fun populateOnViewHolder(receipt: Receipts) {
        itemView.tvReceiptNumber.text = receipt.id.toString()
    }
}