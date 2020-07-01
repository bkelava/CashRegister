package hr.ferit.bozidarkelava.cashregister.miscellaneous

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.view.View
import hr.ferit.bozidarkelava.cashregister.containers.ReceiptDataContainer
import hr.ferit.bozidarkelava.cashregister.containers.TotalPriceContainer
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformation
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipts
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class PDFHelper(private val mFolder: File, private val mContext: Context) {
    private var mFile: File? = null

    private val databaseCompanyInformation = CashRegisterDatabase.getInstance().companyInformationDao()
    private val databaseReceipts = CashRegisterDatabase.getInstance().receiptsDao()

    fun saveImageToPDF(title: View, bitmap: Bitmap, filename: String) {
        mFile = File(mFolder, "$filename.pdf")
        if (!mFile!!.exists()) {
            val height = title.height + bitmap.height
            val document = PdfDocument()
            val mHeight = height+500
            val pageInfo = PageInfo.Builder(bitmap.width, mHeight, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas


            title.draw(canvas)
            canvas.drawBitmap(bitmap,0F,0F, null)
            val paint = Paint()

            paint.color = Color.BLACK
            paint.textSize = 35F
            paint.textAlign = Paint.Align.RIGHT


            canvas.drawText("RECEIPT NO: "+ ReceiptDataContainer.getId().toString(), (3*bitmap.width/4).toFloat(), height.toFloat(), paint)
            canvas.drawText("Total price: " + TotalPriceContainer.getTotalPrice().toString(), (3*bitmap.width/4).toFloat(), height.toFloat()+50, paint)

            val companyInformation: List<CompanyInformation> = databaseCompanyInformation.selectAll()

            canvas.drawText("Company name: "+companyInformation[0].companyName, (3*bitmap.width/4).toFloat(), height.toFloat()+100, paint)
            canvas.drawText("Address: "+companyInformation[0].companyAddress, (3*bitmap.width/4).toFloat(), height.toFloat()+150, paint)
            canvas.drawText("City: " + companyInformation[0].companyCityAndPostal, (3*bitmap.width/4).toFloat(), height.toFloat()+200, paint)

            paint.textSize = 35F
            paint.textAlign= Paint.Align.RIGHT

            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            canvas.drawText("Date and time : " +currentDate+" " + currentTime,  (3*bitmap.width/4).toFloat(), height.toFloat()+250, paint)

            document.finishPage(page)

            val receipt: Receipts = Receipts(ReceiptDataContainer.getId(), mFile!!.path.toString())

            databaseReceipts.insert(receipt)
            try {
                mFile!!.createNewFile()
                val out: OutputStream = FileOutputStream(mFile)
                document.writeTo(out)
                document.close()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    init {
        if (!mFolder.exists()) mFolder.mkdirs()
    }
}