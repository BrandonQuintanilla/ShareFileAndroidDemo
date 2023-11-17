package com.experimental.passwordpdfpoc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.IOException

class PdfManager(private val context: Context) {

    fun renderPdfWithPassword(pdfFile: File, password: String): List<Bitmap> {
        val decryptedFile = decryptPdf(pdfFile, password)
        return renderPdf(decryptedFile)
    }

    private fun decryptPdf(pdfFile: File, password: String): File {
        val decryptedFile = File(context.cacheDir, "decrypted.pdf")
        try {
            val document = PDDocument.load(pdfFile, password)
            document.save(decryptedFile)
            document.close()
        }/* catch (e: PDDocumentPasswordException) {
            e.printStackTrace()
            // La contraseña es incorrecta, puedes manejar esto según tus necesidades
        } catch (e: PDDocumentLoadException) {
            e.printStackTrace()
            // No se pudo cargar el documento PDF, maneja esto según tus necesidades
        } */catch (e: IOException) {
            e.printStackTrace()
        }
        return decryptedFile
    }

    private fun renderPdf(pdfFile: File): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)

        val pdfRenderer = PdfRenderer(fileDescriptor)

        for (pageNumber in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(pageNumber)

            val width = page.width
            val height = page.height

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            bitmaps.add(bitmap)

            page.close()
        }

        pdfRenderer.close()
        fileDescriptor.close()

        return bitmaps
    }
}