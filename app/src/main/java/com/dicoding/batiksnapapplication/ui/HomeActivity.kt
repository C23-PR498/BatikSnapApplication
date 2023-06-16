package com.dicoding.batiksnapapplication.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.dicoding.batiksnapapplication.MainActivity
import com.dicoding.batiksnapapplication.R
import com.dicoding.batiksnapapplication.data.Preference
import com.dicoding.batiksnapapplication.data.repositor.Result
import com.dicoding.batiksnapapplication.data.response.UploadResponse
import com.dicoding.batiksnapapplication.databinding.ActivityHomeBinding
import com.dicoding.batiksnapapplication.ui.list.ListActivity
import com.dicoding.batiksnapapplication.ui.upload.UploadViewModel
import com.dicoding.batiksnapapplication.ui.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var uploadViewModel: UploadViewModel

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

        uploadViewModel =
            ViewModelProvider(this, ViewModelFactory(this)).get(UploadViewModel::class.java)


        val params =
            actionBar?.customView?.layoutParams as androidx.appcompat.app.ActionBar.LayoutParams
        params.gravity = params.gravity and Gravity.CENTER_HORIZONTAL
        actionBar?.customView?.layoutParams = params


        binding.cameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.uploadButton.setOnClickListener {
            uploadImage()
        }
        val fileUri = intent.getParcelableExtra<Uri>("selected_image")
        fileUri?.let { uri ->
            getFile = uri.toFile()
            val rotation = getRotationFromImage(getFile!!.path)
            val rotatedBitmap = BitmapFactory.decodeFile(getFile!!.path).rotateBitmap(rotation)
            binding.previewImageView.setImageBitmap(rotatedBitmap)
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
            val rotation = getRotationFromImage(getFile!!.path)
            val rotatedBitmap = BitmapFactory.decodeFile(getFile!!.path).rotateBitmap(rotation)
            binding.previewImageView.setImageBitmap(rotatedBitmap)
        }
    }

    private fun uploadImage() {
        getFile?.let { file ->
            val fileSizeInMB = file.length() / (1024 * 1024)
            if (fileSizeInMB > 1) {
                Toast.makeText(this@HomeActivity, "Max size 1 MB", Toast.LENGTH_SHORT).show()
                return
            }
            val rotation = getRotationFromImage(file.path)
            val bitmap = BitmapFactory.decodeFile(file.path)
            val rotatedBitmap = bitmap.rotateBitmap(rotation)
            val compressedFile = reduceFileImage(rotatedBitmap)
            val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                compressedFile.name,
                requestImageFile
            )

            uploadViewModel.postImage(imageMultipart).observe(this) {
                if (it != null) {
                    when (it) {
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, "Upload success. Predict: ${it.data}", Toast.LENGTH_SHORT).show()
                            processCreate(it.data)
                        }
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } ?: run {
            Toast.makeText(this@HomeActivity, "Please select an image file first.", Toast.LENGTH_SHORT).show()
        }
    }




    private fun processCreate(data: UploadResponse) {
        if (data.error) {
            Toast.makeText(this@HomeActivity, "Batik Gagal Kirim Foto", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@HomeActivity, "Upload Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("prediction", data.predict)
            startActivity(intent)
            finish()
        }
    }


    private fun showLoading(state: Boolean) {
        binding.pbCreateStory.isVisible = state
        binding.previewImageView.isInvisible = state
        binding.previewImageView.isInvisible = state
        binding.uploadButton.isInvisible = state

    }

    private fun getRotationFromImage(imagePath: String): Int {
        val exif = ExifInterface(imagePath)
        val rotation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun Bitmap.rotateBitmap(degrees: Int): Bitmap {
        if (degrees == 0) {
            return this
        }

        val matrix = Matrix()
        matrix.setRotate(degrees.toFloat())
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun reduceFileImage(bitmap: Bitmap): File {
        val file = File.createTempFile("compressed", ".jpg", cacheDir)
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        out.flush()
        out.close()
        return file
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

}
