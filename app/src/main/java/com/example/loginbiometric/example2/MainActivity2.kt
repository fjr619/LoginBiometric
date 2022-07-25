package com.example.loginbiometric.example2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.example.loginbiometric.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(), BiometricAuthListener {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var pref: SharedPreferences
    private var biometricMode: BiometricMode? = null

    companion object {
        private const val TAG = "BioMetric"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = this.getSharedPreferences("sp", MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        setupBehaviourFormAndButton()
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        Toast.makeText(
            this,
            "Biometric success\nread data login",
            Toast.LENGTH_SHORT
        ).show()

        pref.username = pref.fpusername
        pref.fpusername?.let { pref.fppassword?.let { it1 -> doLogin(it, it1) } }
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        Toast.makeText(this, "Biometric login. Error: $errorMessage", Toast.LENGTH_SHORT)
            .show()
    }

    private fun setupBehaviourFormAndButton() {
        binding.username.setText(pref.username)

        if (pref.username.isNullOrEmpty().not()) {
            binding.username.isEnabled = false
            binding.buttonLogin.text = "Relogin"
            binding.buttonDiffEmail.visibility = View.VISIBLE
        } else {
            binding.username.isEnabled = true
            binding.buttonLogin.text = "login"
            binding.buttonDiffEmail.visibility = View.GONE
        }

        Log.i(
            TAG, "isBiometricReady ${BiometricUtil.isBiometricReady(this)} " +
                    "username ${pref.username.isNullOrEmpty().not()}" +
                    "fpusername ${pref.fpusername.isNullOrEmpty().not()} " +
                    "fppassword ${pref.fppassword.isNullOrEmpty().not()} "
        )

        binding.buttonBiometricsLogin.visibility =
            if (BiometricUtil.isBiometricReady(this) &&
                pref.fpusername.isNullOrEmpty().not() &&
                pref.fppassword.isNullOrEmpty().not() &&
                pref.fpenabled
            ) View.VISIBLE
            else View.GONE

        if (BiometricUtil.isBiometricReady(this) &&
            (pref.username.isNullOrEmpty().not() &&
                    pref.fppassword.isNullOrEmpty().not() &&
                    pref.fppassword.isNullOrEmpty().not() &&
                    pref.fpenabled)
        ) {
            //relogin dan punya data login fingerprint
            showBiometricPrompt()
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

    //simulate login process
    private fun doLogin(
        userName: String,
        password: String,
    ) {
        Toast.makeText(
            this,
            "Do login\nusername $userName, password $password",
            Toast.LENGTH_SHORT
        )
            .show()

        //save data login
        pref.username = userName

        if (userName != pref.fpusername) {
            pref.fpenabled = false
        }

        pref.fpusername = userName
        pref.fppassword = password

        finish()
        startActivity(Intent(this, PdfViewerActtivity::class.java))
    }

    //simulate biometric login
    fun onClickBiometrics(view: View) {
        biometricMode = BiometricMode.READ
        showBiometricPrompt()
    }

    //simulate normal login
    fun onClickLogin(view: View) {
        val userName = binding.username.text.toString()
        val password = binding.password.text.toString()
        if (userName.isNotEmpty() &&
            password.isNotEmpty()
        ) {
            doLogin(userName, password)
        }
    }

    //simulate klo relogin dan ingin ganti email
    fun onClickDiffEmail(view: View) {
        pref.username = null
        binding.password.setText("")
        setupBehaviourFormAndButton()
    }
}