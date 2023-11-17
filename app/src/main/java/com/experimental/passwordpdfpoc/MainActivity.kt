package com.experimental.passwordpdfpoc

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        /*
        val mjson = readRawContent(R.raw.jsondata)

        val data: ResponseModel = Gson().fromJson(mjson, ResponseModel::class.java)


        val pdf = PDDocument.load(
            data.responseData.pdf.trim().toByteArray(),
            data.responseData.pdfkey
        )
        */
        val rawFile = getRawFileAsFile(R.raw.jsondata)
        val pdf = PDDocument.load(
            rawFile,
           "CUMJ930415"
        )

        pdf.isAllSecurityToBeRemoved = true

        val decryptedFile = File(this.cacheDir, "decrypted${System.currentTimeMillis()}.pdf")
        pdf.save(decryptedFile)
        pdf.close()

        val v: PDFView = findViewById(R.id.pdfView)
        v.fromFile(decryptedFile)
        /* val v: PDFView = findViewById(R.id.pdfView)
         v
             .fromBytes(data.responseData.pdf.toByteArray())
             .password(data.responseData.password)
             .load()
         */
        /*

        val wv:WebView = findViewById(R.id.main_web_view)



        //val pd =PdfDocument()
        val base64EncodedPDF = "data:application/pdf;base64,${data.responseData.pdf}"
        //val password = "mypassword"
        val dataURL = "$base64EncodedPDF#password=${data.responseData.password}"

        wv.settings.javaScriptEnabled = true
        wv.loadUrl(dataURL)*/
    }

    fun getRawFileAsFile(resourceId: Int): File? {
        val context: Context = this // Replace 'this' with your activity or application context
        try {
            val resources = context.resources
            val inputStream = resources.openRawResource(resourceId)
            val fileName = resources.getResourceEntryName(resourceId)
            val file = File(context.filesDir, fileName)
            val outputStream: OutputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            outputStream.close()
            if (file.exists()) {
                return file
            }
        } catch (e: IOException) {
            // Handle any exceptions that occur during reading, writing, or closing the streams
            e.printStackTrace()
        }
        return null // Return null if there was an error during the file operations
    }

    fun readRawContent(resourceId: Int): String? {
        val context: Context = this // Replace 'this' with your activity or application context
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