package com.sedata.imprimivoire.qrgenerator.helper

import android.R.attr
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream


class BitmapHelper {

    companion object {

        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos)
            val b = baos.toByteArray()
            val imageSize = baos.toByteArray().size / 1024
            return b
        }
    }
}