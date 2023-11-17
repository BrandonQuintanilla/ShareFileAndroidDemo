package com.experimental.passwordpdfpoc

import android.app.DownloadManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.pdfview.PDFView
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mjson = readRawContent(R.raw.jsondata)
        val data: ResponseModel = Gson().fromJson(mjson, ResponseModel::class.java)

        val decodedBytes: ByteArray = Base64.decode(data.responseData.pdf, Base64.DEFAULT)
        val pdf = PDDocument.load(
            decodedBytes, data.responseData.pdfkey
        )

        pdf.isAllSecurityToBeRemoved = true

        val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val decryptedFile = File(publicDir, "decrypted${System.currentTimeMillis()}.pdf")
        pdf.save(decryptedFile)
        pdf.close()

        val v: PDFView = findViewById(R.id.pdfView)
        v.fromFile(decryptedFile)
        v.show()

        val btn: Button = findViewById(R.id.btn_share)
        btn.setOnClickListener {
            shareFile(decryptedFile)
        }
    }

    private fun shareFile(privateFile: File) {
        val fileUri = FileProvider.getUriForFile(this, "$packageName.provider", privateFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        //shareIntent.setType("application/pdf")
        shareIntent.setDataAndType(fileUri, "application/*")
        startActivity(Intent.createChooser(shareIntent, "Share PDF file"))
    }
    private fun readRawContent(resourceId: Int): String? {
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