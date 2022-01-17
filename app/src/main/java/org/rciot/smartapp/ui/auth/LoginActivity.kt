package org.rciot.smartapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.rciot.smartapp.databinding.ActivityLoginBinding
import org.rciot.smartapp.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLogin.setOnClickListener {
                setData()
            }
            tvSigninHint.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
            tvGoogle.setOnClickListener {
                Snackbar.make(
                    binding.root,
                    "SignIn Google masih dalam pengembangan",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setData() {
        val usernameData = binding.edtLogin.text.toString()
        val passwordData = binding.edtPass.text.toString()

        checkInputData(usernameData, passwordData)
    }

    private fun checkInputData(username: String, password: String) = with(binding) {
        when {
            TextUtils.isEmpty(username) -> edtLogin.apply {
                error = "Please fill your username"
                requestFocus()
            }
            TextUtils.isEmpty(password) -> edtPass.apply {
                error = "Please fill your password"
                requestFocus()
            }
            else -> {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}