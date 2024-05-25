package com.dicoding.asclepius.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.ResultEntity
import com.dicoding.asclepius.databinding.ResultCardBinding
import com.dicoding.asclepius.view.ResultActivity
import java.text.NumberFormat

class HistoryAdapter(private val context: Context) : ListAdapter<ResultEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultEntity>() {
            override fun areItemsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ResultCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(private val binding: ResultCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultEntity) {
            val inputStream = context.openFileInput(result.imagename)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            with(binding){
                tvLabelName.text = context.getString(R.string.history_label_score, result.label, NumberFormat.getPercentInstance()
                    .format(result.score).trim())
                tvDate.text = result.date
                imgItemPhoto.setImageBitmap(bitmap)
            }
            inputStream.close()
//            Glide.with(context)
//                .load(result.avatarUrl)
//                .placeholder(R.drawable.baseline_person_24)
//                .into(binding.imgItemPhoto)
//
            binding.cardView.setOnClickListener{
                val intent = Intent(context, ResultActivity::class.java)
                intent.putExtra("saved_image", result.imagename)
                context.startActivity(intent)
            }
        }
    }
}