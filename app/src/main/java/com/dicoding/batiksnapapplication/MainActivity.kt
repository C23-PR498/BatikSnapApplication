package com.dicoding.batiksnapapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.batiksnapapplication.databinding.ActivityMainBinding
import com.dicoding.batiksnapapplication.ui.CameraActivity
import com.dicoding.batiksnapapplication.ui.UploadActivity  // Import UploadActivity
import com.dicoding.batiksnapapplication.ui.utils.uriToFile
import java.io.File

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
