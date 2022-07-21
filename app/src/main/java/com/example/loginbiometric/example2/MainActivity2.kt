package com.example.loginbiometric.example2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.example.loginbiometric.databinding.ActivityMain2Binding

//ini digunakan untuk check apakah relogin atau login biasa
var SharedPreferences.username
    get() = getString("username", "")
    set(value) {
        edit().putString("username", value).apply()
    }

//save di encrypted pref, ini digunakan untuk kebutuhan login menggunakan biometric
var SharedPreferences.fpusername
    get() = getString("fpusername", "")
    set(value) {
        edit().putString("fpusername", value).apply()
    }

//save di encrypted pref, ini digunakan untuk kebutuhan login menggunakan biometric
var SharedPreferences.fppassword
    get() = getString("fppassword", "")
    set(value) {
        edit().putString("fppassword", value).apply()
    }

//untuk state dari biometric
enum class BiometricMode {
    SAVE, READ
}

class MainActivity2 : AppCompatActivity(), BiometricAuthListener {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var pref: SharedPreferences
    private var biometricMode: BiometricMode? = null

    companion object {
        private const val TAG = "BioMetric"
    }

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = this.getSharedPreferences("sp", MODE_PRIVATE)
        creeateDialogToUseBiometric()
    }

    override fun onResume() {
        super.onResume()
        setupBehaviourFormAndButton()
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        Toast.makeText(
            this,
            "Biometric success",
            Toast.LENGTH_SHORT
        )

        //simulate behaviour after biometric succeed
        //READ -> baca pref fp dan simulate login
        if (biometricMode == BiometricMode.READ) {
            Toast.makeText(
                this,
                "read data login",
                Toast.LENGTH_SHORT
            )
                .show()
            pref.username = pref.fpusername
            pref.fpusername?.let { pref.fppassword?.let { it1 -> doLogin(it, it1) } }
        } else {
            //SAVE -> simulate login, kalo succeed baru save pref fp
            doLogin(binding.username.text.toString(), binding.password.text.toString()) { username, password ->
                pref.fpusername = username
                pref.fppassword = password

                Toast.makeText(this, "save data login", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        Toast.makeText(this, "Biometric login. Error: $errorMessage", Toast.LENGTH_SHORT)
            .show()
    }

    private fun creeateDialogToUseBiometric() {
        alertDialog = this.run {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("yes") { _, _ ->
                    // User clicked OK button
                    // keluarkan fingerprint dialog dan save data login ke encrypted pref
                    biometricMode = BiometricMode.SAVE
                    showBiometricPrompt()
                }
                setNegativeButton(
                    "no"
                ) { dialog, id ->
                    // User cancelled the dialog
                    dialog.dismiss()
                    doLogin(binding.username.text.toString(), binding.password.text.toString())
                }

                setTitle("Do you want to enable login with fingerprint?")
            }

            // Create the AlertDialog
            builder.create()
        }
    }

    private fun setupBehaviourFormAndButton() {
        Log.i(TAG, "BiometricUtil.isBiometricReady(this) ${BiometricUtil.isBiometricReady(this)}")
        Log.i(TAG, "pref.fpusername ${pref.fpusername} pref.fppassword ${pref.fppassword} ")
        binding.username.setText(pref.username)

        if (pref.username.isNullOrEmpty().not()) {
            binding.username.isEnabled = false
            binding.buttonLogin.text = "Relogin"
            binding.buttonLogout.visibility = View.VISIBLE
        } else {
            binding.username.isEnabled = true
            binding.buttonLogin.text = "login"
            binding.buttonLogout.visibility = View.GONE
        }

        binding.buttonBiometricsLogin.visibility =
            if (BiometricUtil.isBiometricReady(this) &&
                pref.fpusername.isNullOrEmpty().not() &&
                pref.fppassword.isNullOrEmpty().not()
            ) View.VISIBLE
            else View.GONE

        binding.buttonReset.visibility =
            if (pref.fpusername.isNullOrEmpty() && pref.fppassword.isNullOrEmpty())
                View.GONE else View.VISIBLE
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
    private fun doLogin(userName: String, password: String, doSomething: (String, String) -> Unit = {_,_ ->}) {
        if (userName == "aa" && password == "bb") {
            Toast.makeText(
                this,
                "Do login\nusername $userName, password $password",
                Toast.LENGTH_SHORT
            )
                .show()

//            binding.password.setText("")
//            setupBehaviourFormAndButton()

            doSomething.invoke(userName, password)
            finish()
            startActivity(Intent(this, PdfViewerActtivity::class.java))
        } else {
            Toast.makeText(
                this,
                "Data salah, harus pakai data ini\nusername aa, password bb",
                Toast.LENGTH_SHORT
            )
                .show()
        }
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
            if (BiometricUtil.isBiometricReady(this) &&
                pref.username.isNullOrEmpty() ||
                (pref.fppassword.isNullOrEmpty() &&
                pref.fppassword.isNullOrEmpty())) {
                pref.username = binding.username.text.toString()
                alertDialog.show()
            } else {
                pref.username = binding.username.text.toString()
                doLogin(binding.username.text.toString(), binding.password.text.toString())
            }
        }
    }

    //simulate klo relogin dan ingin ganti email
    fun onClickLogout(view: View) {
        pref.username = null
        binding.password.setText("")
        setupBehaviourFormAndButton()
    }

    //simulate reset data fingerprint (local)
    fun onClickReset(view: View) {
        pref.fpusername = null
        pref.fppassword = null
        setupBehaviourFormAndButton()
    }


}