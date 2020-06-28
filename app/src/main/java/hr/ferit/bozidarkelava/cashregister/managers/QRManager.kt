package hr.ferit.bozidarkelava.cashregister.managers

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRManager {

    private var multiFormatWriter: MultiFormatWriter = MultiFormatWriter()

    fun createQR(text: String): Bitmap {
        val bitMatrix: BitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500)
        val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
        val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
        return bitmap
    }
}