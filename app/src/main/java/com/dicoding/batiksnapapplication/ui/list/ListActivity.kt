package com.dicoding.batiksnapapplication.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.batiksnapapplication.R
import com.dicoding.batiksnapapplication.data.Preference
import com.dicoding.batiksnapapplication.databinding.ActivityDetailBinding
import com.dicoding.batiksnapapplication.databinding.ActivityListBinding
import com.dicoding.batiksnapapplication.ui.SettingActivity
import com.dicoding.batiksnapapplication.ui.ViewModelFactory
import com.dicoding.batiksnapapplication.ui.detail.DetailActivity
import com.dicoding.batiksnapapplication.ui.list.adapter.ListAdapter

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding

    private lateinit var bindingDetail: ActivityDetailBinding

    private val listViewModel: ListViewModel by viewModels {
        ViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        bindingDetail = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postponeEnterTransition()

        val prediction = intent.getStringExtra("prediction") ?: ""
        listViewModel.result_predict = prediction


        binding.listbatik.layoutManager = LinearLayoutManager(this)
        val adapter = ListAdapter()
        adapter.intent =
            Intent(this, DetailActivity::class.java).apply {
                putExtra("pred", prediction)
            }

        binding.listbatik.adapter = adapter
        listViewModel.getBatik()
        listViewModel.batik.observe(this, {
            adapter.submitList(it)
        })
        setupBackPressed()
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                Preference.logOut(this)
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}