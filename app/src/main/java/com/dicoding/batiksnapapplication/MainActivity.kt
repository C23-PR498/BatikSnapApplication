package com.dicoding.batiksnapapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import com.dicoding.batiksnapapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Custom ActionBar
        val actionBar = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custom_title, null)
        val textView = view.findViewById<TextView>(R.id.actionbar_title)
        textView.text = "Batik Snap"
        actionBar?.customView = view


        val params = actionBar?.customView?.layoutParams as androidx.appcompat.app.ActionBar.LayoutParams
        params.gravity = params.gravity and Gravity.CENTER_HORIZONTAL
        actionBar?.customView?.layoutParams = params
    }
}
