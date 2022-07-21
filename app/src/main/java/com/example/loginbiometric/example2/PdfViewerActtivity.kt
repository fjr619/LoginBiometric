package com.example.loginbiometric.example2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginbiometric.databinding.ActivityPdfviewerBinding
import com.github.barteksc.pdfviewer.util.FitPolicy

class PdfViewerActtivity: AppCompatActivity() {

    private lateinit var binding: ActivityPdfviewerBinding

    private val SAMPLE_FILE = "tekenaja.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.pdfView.fromAsset(SAMPLE_FILE)
           .defaultPage(0)
           .spacing(10)
           .enableAnnotationRendering(true)
           .enableDoubletap(true)
           .pageFitPolicy(FitPolicy.BOTH)
           .load()
    }
}