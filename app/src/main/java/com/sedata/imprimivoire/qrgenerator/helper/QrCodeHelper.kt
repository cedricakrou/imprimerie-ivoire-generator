package com.sedata.imprimivoire.qrgenerator.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.util.Log
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.zxing.WriterException

class QrCodeHelper {

    companion object {


        fun createQrCode(context: Context, message: ByteArray): Bitmap? {

            val size = message.size

            var qrgEncoder: QRGEncoder? = null
            var bitmapQrCode: Bitmap? = null
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val width = point.x
            val height = point.y
            var smallerDimension = if (width > height) width else height
            qrgEncoder = QRGEncoder(
                message.toString(charset = Charsets.US_ASCII),
                null,
                QRGContents.Type.TEXT,
                smallerDimension
            )
            bitmapQrCode = qrgEncoder.bitmap

            return bitmapQrCode
        }
    }
}