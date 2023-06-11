package com.dicoding.batiksnapapplication.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.isInvisible
import com.dicoding.batiksnapapplication.databinding.ActivityUploadBinding
import com.dicoding.batiksnapapplication.ui.utils.rotateBitmap
import com.dicoding.batiksnapapplication.ui.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // get the URI from intent
        val selectedImageUriString = intent.getStringExtra("selectedImageUri")
        val selectedImageUri = Uri.parse(selectedImageUriString)
        // set the image view
        binding.secondImageView.setImageURI(selectedImageUri)

        binding.uploadButton.setOnClickListener {
            uploadImage()
        }

        val fileUri = intent.getParcelableExtra<Uri>("selected_image")
        fileUri?.let { uri ->
            val isBackCamera = intent.getBooleanExtra("isBackCamera", false)
            val result = rotateBitmap(
                BitmapFactory.decodeFile(uri.path),
                isBackCamera
            )
            getFile = uri.toFile()
            binding.secondImageView.setImageBitmap(result)
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
            binding.secondImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        getFile?.let { file ->
            val fileSizeInMB = file.length() / (1024 * 1024)
            if (fileSizeInMB > 1) {
                Toast.makeText(this@UploadActivity, "Max size 1 MB", Toast.LENGTH_SHORT).show()
                return
            }


            showLoading(true)
            val rotation = getRotationFromImage(file.path)
            val bitmap = BitmapFactory.decodeFile(file.path)
            val rotatedBitmap = bitmap.rotateBitmap(rotation)
            val compressedFile = reduceFileImage(rotatedBitmap)

            val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                compressedFile.name,
                requestImageFile
            )


        } ?: run {
            Toast.makeText(this@UploadActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }


//    private fun processCreate(data: PostStoryResponse) {
//        if (data.error) {
//            Toast.makeText(this@UploadActivity, "Tambah Story Gagal", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(
//                this@UploadActivity,
//                "Story berhasil dibuat ayok lihat story",
//                Toast.LENGTH_LONG
//            ).show()
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    private fun showLoading(state: Boolean) {
//        binding.pbCreateStory.isVisible = state
        binding.previewImageView.isInvisible = state
        binding.secondImageView.isInvisible = state
        binding.uploadButton.isInvisible = state

    }

    private fun getRotationFromImage(imagePath: String): Int {
        val exif = ExifInterface(imagePath)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}