package com.sedata.imprimivoire.qrgenerator

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.zxing.WriterException
import com.sedata.imprimivoire.qrgenerator.data.QrCodeData
import com.sedata.imprimivoire.qrgenerator.helper.BitmapHelper
import com.sedata.imprimivoire.qrgenerator.helper.DataCompressionHelper
import com.sedata.imprimivoire.qrgenerator.helper.QrCodeHelper


class MainActivity : AppCompatActivity() {


    private lateinit var edt_firstName: TextInputEditText
    private lateinit var edt_lastName: TextInputEditText
    private lateinit var btn_select_image: Button
    private lateinit var btn_generate_qr_code: Button

    private lateinit var img_preview_image: ImageView
    private lateinit var img_preview_qr_code: ImageView

    val password = "imprimerie-natio"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edt_lastName = findViewById(R.id.edt_lastName)
        edt_firstName = findViewById(R.id.edt_firstName)
        btn_select_image = findViewById(R.id.btn_select_image)
        btn_generate_qr_code = findViewById(R.id.btn_generate_qr_code)
        img_preview_image = findViewById(R.id.img_preview_image)
        img_preview_qr_code = findViewById(R.id.img_preview_qr_code)

        btn_select_image.setOnClickListener {

            imageChooser()
        }

        btn_generate_qr_code.setOnClickListener {

            val firstName: String = edt_firstName.text.toString()
            val lastName: String = edt_lastName.text.toString()

            val image: Bitmap = img_preview_image.drawable.toBitmap()

            // conversion de l'image en base64
            val photo: ByteArray = BitmapHelper.bitmapToByteArray(image)

            // constitution de l'objet Qr Code
            val qrCodeData = QrCodeData(
                firstName,
                lastName,
                photo
            )

            // Creation du Json
            val qrCodeDataToString = Gson().toJson(qrCodeData)
//            val qrCodeDataCompress = DataCompressionHelper.compressObject(qrCodeDataToString)

            // Encrypt Data
//            val secretKey = DataSecurityHelper.secretKey(password)
//            val encryptedQrCodeData: ByteArray = DataSecurityHelper.encrypt(qrCodeDataCompress.toString(Charsets.UTF_8), secretKey)


//            val mWriter = MultiFormatWriter()

            try {

                // Compression
                var qrCodeContentCompress: ByteArray = DataCompressionHelper.compressObject(qrCodeDataToString)

                var size = qrCodeContentCompress.size

                qrCodeContentCompress = photo

                while (size > 1 ){

                    qrCodeContentCompress = DataCompressionHelper.compressObject(String(qrCodeContentCompress, Charsets.US_ASCII))

                    size = qrCodeContentCompress.size / 1024
                }

//                val qrCodeContent = String(qrCodeContentCompress, Charsets.ISO_8859_1)
                val mBitmap = QrCodeHelper.createQrCode(this, qrCodeContentCompress)

                img_preview_qr_code.visibility = View.VISIBLE//creating bitmap of code
                img_preview_qr_code.setImageBitmap(mBitmap) //Setting generated QR code to imageView
                // to hide the keyboard
//                val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                manager.hideSoftInputFromWindow(img_preview_image.getApplicationWindowToken(), 0)

//                val b = BitmapFactory.decodeByteArray(qrCodeDataCompress, 0, qrCodeContentCompress.size)

//                img_preview_qr_code.setImageBitmap(b)

            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }

    fun imageChooser() {

        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                val selectedImageUri: Uri = data?.data!!
                img_preview_image.visibility = View.VISIBLE
                img_preview_image.setImageURI(selectedImageUri)
            }
        }
    }
}