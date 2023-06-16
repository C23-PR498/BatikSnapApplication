package com.dicoding.batiksnapapplication.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.dicoding.batiksnapapplication.data.repositor.Result
import com.dicoding.batiksnapapplication.databinding.ActivityDetailBinding
import com.dicoding.batiksnapapplication.ui.ViewModelFactory
import com.dicoding.batiksnapapplication.ui.list.ListViewModel
import com.dicoding.batiksnapapplication.ui.utils.loadImage

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val listViewModel: ListViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postponeEnterTransition()

        retrieveBatikFromIntent()

        setupActionBar()
    }

    private fun retrieveBatikFromIntent() {
        val id = intent.getIntExtra("id", 1)
        val pred = intent.getStringExtra("pred") ?: ""
        listViewModel.getDetail(id, pred).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
//                        showLoading(true)
                        Log.d("Detail Ac", "retrieveBatikFromIntent: loading")
                    }
                    is Result.Success -> {
                        binding.tvDescription.text = result.data.deskripsi
                        binding.titleBatik.text = result.data.nama
                        binding.imgBatik.loadImage(result.data.image)
                        binding.tvAsal.text = result.data.asal
                        binding.tvSejarah.text = result.data.sejarah
//                        showLoading(false)
                    }
                    is Result.Error -> {
//                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

//    private fun showLoading(state: Boolean) {
//        binding.apply {
//            imgBatik.isVisible = state
//            titleBatik.isVisible = state
//            descBatik.isVisible = state
//            tvAsal.isVisible = state
//            tvSejarah.isVisible = state
//
//        }
//    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Batik Snap"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}