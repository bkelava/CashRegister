package hr.ferit.bozidarkelava.cashregister.recyclerViews.viewReceiptsRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipts
import hr.ferit.bozidarkelava.cashregister.interfaces.ReceiptButtonClick
import hr.ferit.bozidarkelava.cashregister.recyclerViews.invoiceRecyclerView.InvoiceViewHolder
import kotlinx.android.synthetic.main.item_view_receipts.view.*

class ViewReceiptsRecyclerAdapter(receipts: MutableList<Receipts>, clicks: ReceiptButtonClick, context: Context):
    RecyclerView.Adapter<ViewReceiptsViewHolder>() {

    private var receipts: MutableList<Receipts>
    private var clicks: ReceiptButtonClick
    private var context: Context

    init {
        this.receipts=receipts
        this.clicks=clicks
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewReceiptsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_receipts, parent, false)
        val holder = ViewReceiptsViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        val size = receipts.size
        return size
    }

    override fun onBindViewHolder(holder: ViewReceiptsViewHolder, position: Int) {
        val receipt = receipts[position]
        holder.populateOnViewHolder(receipt)

        holder.itemView.btnOpenReceipt.setOnClickListener() {
            clicks.openReceipt(position)
        }
    }


}