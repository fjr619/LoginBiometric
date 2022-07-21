package com.example.loginbiometric.example2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginbiometric.databinding.ActivityPdfviewerBinding

class PdfViewerActtivity: AppCompatActivity() {

    private lateinit var binding: ActivityPdfviewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}