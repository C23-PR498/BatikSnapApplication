package com.dicoding.batiksnapapplication.ui.list.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.batiksnapapplication.data.Batik
import com.dicoding.batiksnapapplication.databinding.ItemBatikBinding
import com.dicoding.batiksnapapplication.ui.utils.loadImage

class ListAdapter
    : ListAdapter<Batik, com.dicoding.batiksnapapplication.ui.list.adapter.ListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    var intent: Intent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBatikBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, intent!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemBatikBinding, intent: Intent) :
        RecyclerView.ViewHolder(binding.root) {
        val intent = intent
        fun bind(data: Batik) {
            binding.ivItemPhoto.loadImage(data.image)
            binding.tvItemName.text = data.nama
            binding.tvStoryDesc.text = data.deskripsi

            binding.ivItemPhoto.setOnClickListener {
                binding.root.context.startActivity(intent.putExtra("id", data.id))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Batik>() {
            override fun areItemsTheSame(oldItem: Batik, newItem: Batik): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Batik, newItem: Batik): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}