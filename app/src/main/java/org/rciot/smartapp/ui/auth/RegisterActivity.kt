package org.rciot.smartapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.rciot.smartapp.R
import org.rciot.smartapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}