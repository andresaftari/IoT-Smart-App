package org.rciot.smartapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.rciot.smartapp.databinding.ActivityRegisterBinding
import org.rciot.smartapp.ui.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnSignup.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }
            tvSignupHint.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
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

        setData()
    }

    private fun setData() {
        val usernameData = binding.edtLogin.text.toString()
        val fullnameData = binding.edtFullname.text.toString()
        val emailData = binding.edtEmail.text.toString()
        val passwordData = binding.edtPass.text.toString()
        val cPasswordData = binding.edtCpass.text.toString()

        checkInputData(usernameData, fullnameData, emailData, passwordData, cPasswordData)
    }

    private fun checkInputData(
        username: String,
        fullname: String,
        email: String,
        password: String,
        cPassword: String
    ) = with(binding) {
        when {
            TextUtils.isEmpty(username) -> edtLogin.apply {
                error = "Please fill username"
                requestFocus()
            }
            TextUtils.isEmpty(fullname) -> edtFullname.apply {
                error = "Please fill your fullname"
                requestFocus()
            }
            TextUtils.isEmpty(email) -> edtEmail.apply {
                error = "Please fill your email"
                requestFocus()
            }
            TextUtils.isEmpty(password) -> edtPass.apply {
                error = "Please fill password"
                requestFocus()
            }
            TextUtils.isEmpty(cPassword) -> edtPass.apply {
                error = "Please fill confirm password"
                requestFocus()
            }
            else -> startActivity(
                Intent(
                    this@RegisterActivity,
                    MainActivity::class.java
                )
            )
        }
    }
}