package com.example.loginbiometric.example1

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.loginbiometric.R

class MainActivity : AppCompatActivity() {

    private lateinit var biometricPromptManager: BiometricPromptManager

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        biometricPromptManager = BiometricPromptManager(this)

        val secureText = "{\n" +
                "  \"username\": \"franky.wijanarko@cicil.co.id\",\n" +
                "  \"password\": \"123123ASD\"\n" +
                "}"
        findViewById<TextView>(R.id.textView).text = "secureText"
        findViewById<Button>(R.id.buttonEncrypt).setOnClickListener {
            biometricPromptManager.encryptPrompt(
                data = secureText.toByteArray(),
                failedAction = { showToast("encrypt failed") },
                successAction = {
                    findViewById<TextView>(R.id.textView).text = String(it)
                    showToast("encrypt success")
                }
            )
        }

        findViewById<Button>(R.id.buttonDecrypt).setOnClickListener {
            biometricPromptManager.decryptPrompt(
                failedAction = { showToast("decrypt failed") },
                successAction = {
                    findViewById<TextView>(R.id.textView).text = String(it)
                    showToast("decrypt success, do login")
                }
            )
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}