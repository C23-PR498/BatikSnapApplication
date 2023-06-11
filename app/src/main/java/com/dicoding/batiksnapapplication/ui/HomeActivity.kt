package com.dicoding.batiksnapapplication.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.batiksnapapplication.MainActivity
import com.dicoding.batiksnapapplication.R
import com.dicoding.batiksnapapplication.data.Preference
import com.dicoding.batiksnapapplication.databinding.ActivityHomeBinding
import com.dicoding.batiksnapapplication.ui.utils.uriToFile
import java.io.File

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
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

        // Add code to handle click on "cameraButton"
        binding.cameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
    }

    private var getFile: File? = null
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                Preference.logOut(this)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile

            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra("selectedImageUri", selectedImg.toString())
            startActivity(intent)
        }
    }
}
