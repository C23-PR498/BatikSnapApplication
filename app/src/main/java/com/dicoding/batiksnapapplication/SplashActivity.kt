package com.dicoding.batiksnapapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.batiksnapapplication.data.Preference
import com.dicoding.batiksnapapplication.databinding.ActivityMainBinding
import com.dicoding.batiksnapapplication.databinding.ActivitySplashBinding
import com.dicoding.batiksnapapplication.ui.HomeActivity
import javax.xml.datatype.DatatypeConstants.DURATION

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    companion object {
        private const val DURATION: Long = 1500
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveData()


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

    }

    private fun saveData() {
        val sharedPref = Preference.initPref(this, "onSignIn")
        val access_token = sharedPref.getString("access_token", "")

        var intent = Intent(this, MainActivity::class.java)
        if (access_token != "") {
            intent = Intent(this, HomeActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, DURATION)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, DURATION)
        }
    }
}