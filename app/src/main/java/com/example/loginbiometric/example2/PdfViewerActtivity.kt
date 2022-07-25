package com.example.loginbiometric.example2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.example.loginbiometric.databinding.ActivityPdfviewerBinding
import com.github.barteksc.pdfviewer.util.FitPolicy

class PdfViewerActtivity: AppCompatActivity(), BiometricAuthListener {

    private lateinit var binding: ActivityPdfviewerBinding
    private lateinit var pref: SharedPreferences


    private val SAMPLE_FILE = "tekenaja.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        pref = this.getSharedPreferences("sp", MODE_PRIVATE)

        setContentView(binding.root)

       binding.pdfView.fromAsset(SAMPLE_FILE)
           .defaultPage(0)
           .spacing(10)
           .enableAnnotationRendering(true)
           .enableDoubletap(true)
           .pageFitPolicy(FitPolicy.BOTH)
           .load()

        binding.switcher.isChecked = pref.fpenabled

        binding.switcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showBiometricPrompt()
            } else {
                pref.fpenabled = false
                pref.fpusername = null
                pref.fppassword = null
            }
        }

    }

    private fun showBiometricPrompt() {
        BiometricUtil.showBiometricPrompt(
            activity = this,
            listener = this,
            cryptoObject = null,
            allowDeviceCredential = true
        )
    }

    override fun onResume() {
        super.onResume()
        binding.switcher.visibility = if (BiometricUtil.isBiometricReady(this)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    //simulate klo relogin dan ingin ganti email
    fun onClickLogout(view: View) {
        pref.username = null
        finish()
        startActivity(Intent(this,MainActivity2::class.java))
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        binding.switcher.isChecked = true
        pref.fpenabled = true
        Toast.makeText(
            this,
            "Biometric success, enabled biometric login\n",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        binding.switcher.isChecked = false
    }
}