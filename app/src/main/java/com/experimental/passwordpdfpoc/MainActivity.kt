package com.experimental.passwordpdfpoc

import android.content.Context
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.pdfview.PDFView
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mjson = readRawContent(R.raw.jsondata)
        val data: ResponseModel = Gson().fromJson(mjson, ResponseModel::class.java)

        val decodedBytes: ByteArray = Base64.decode(data.responseData.pdf, Base64.DEFAULT)
        val pdf = PDDocument.load(
            decodedBytes,
            data.responseData.pdfkey
        )

        pdf.isAllSecurityToBeRemoved = true

        val decryptedFile = File(this.cacheDir, "decrypted${System.currentTimeMillis()}.pdf")
        pdf.save(decryptedFile)
        pdf.close()

        val v: PDFView = findViewById(R.id.pdfView)
        v.fromFile(decryptedFile)
        v.show()

    }

    fun readRawContent(resourceId: Int): String? {
        val context: Context = this
        try {
            val resources = context.resources
            val inputStream = resources.openRawResource(resourceId)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            val content = StringBuilder()
            while (reader.readLine().also { line = it } != null) {
                content.append(line)
            }
            reader.close()
            return content.toString()
        } catch (e: IOException) {
            // Handle any exceptions that occur during reading or closing the file
            e.printStackTrace()
        }
        return null // Return null if there was an error reading the file
    }
}