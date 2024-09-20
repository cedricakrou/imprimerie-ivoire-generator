package com.sedata.imprimivoire.qrgenerator.helper

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class DataCompressionHelper {

    companion object {


        fun compressObject(obj: Any): ByteArray {
            val byteStream = ByteArrayOutputStream()
            GZIPOutputStream(byteStream, 200).use { gzipStream ->
                ObjectOutputStream(gzipStream).use { it.writeObject(obj) }
            }
            return byteStream.toByteArray()
        }

        fun decompressObject(compressedData: ByteArray): Any {
            val byteStream = ByteArrayInputStream(compressedData)
            GZIPInputStream(byteStream).use { gzipStream ->
                ObjectInputStream(gzipStream).use { return it.readObject() }
            }
        }
    }
}