package org.rciot.smartapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.rciot.smartapp.databinding.ActivitySplashBinding
import org.rciot.smartapp.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : Thread() {
            override fun run() {
                try {
                    sleep(4000)
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                } catch (ex: Exception) {
                    Log.d("SplashIsFailed", "Kalo Gagal")
                }
            }
        }.start()
    }
}