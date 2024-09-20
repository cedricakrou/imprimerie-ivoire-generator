package com.sedata.imprimivoire.qrgenerator.helper

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class DataSecurityHelper {

    companion object {

        fun secretKey(password: String): SecretKey {

            return SecretKeySpec(password.toByteArray(), "AES")
        }

        // Encryption function
        fun encrypt(plainText: String, secretKey: SecretKey): ByteArray {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            return cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        }

        fun decrypt(cipherText: ByteArray, secretKey: SecretKey): String {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decryptedData = cipher.doFinal(cipherText)
            return String(decryptedData, Charsets.UTF_8)
        }
    }
}