package org.rciot.smartapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.rciot.smartapp.databinding.ActivityWelcomeBinding
import org.rciot.smartapp.ui.auth.LoginActivity
import org.rciot.smartapp.ui.auth.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStarted.setOnClickListener {
            startActivity(
                Intent(
                    this@WelcomeActivity,
                    MainActivity::class.java
                )
            )
            finish()
        }
    }
}