package com.dicoding.asclepius.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.NewsCardBinding
import com.dicoding.asclepius.responses.ArticlesItem

class NewsAdapter(private val context: Context) : ListAdapter<ArticlesItem, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(private val binding: NewsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.tvSource.text = article.source?.name
            binding.tvTitle.text = article.title
            binding.tvTitle.text = article.description
            Glide.with(context)
                .load(article.urlToImage)
                .placeholder(R.drawable.ic_place_holder)
                .into(binding.imgItemPhoto)

//            binding.cardView.setOnClickListener{
//                val intent = Intent(context, UserDetailActivity::class.java)
//                intent.putExtra(UserDetailActivity.EXTRA_USER, user)
//                context.startActivity(intent)
//            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}