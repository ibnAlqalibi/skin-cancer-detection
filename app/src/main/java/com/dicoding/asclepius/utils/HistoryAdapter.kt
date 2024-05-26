package com.dicoding.asclepius.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.ResultEntity
import com.dicoding.asclepius.databinding.ResultCardBinding
import com.dicoding.asclepius.view.ResultActivity
import com.dicoding.asclepius.viewmodels.HistoryViewModel
import com.dicoding.asclepius.viewmodels.HistoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat

class HistoryAdapter(private val context: Context) : ListAdapter<ResultEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
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
            val historyViewModel = obtainViewModel(context as AppCompatActivity)

            val inputStream = context.openFileInput(result.imagename)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            with(binding){
                tvLabelName.text = context.getString(R.string.history_label_score, result.label, NumberFormat.getPercentInstance()
                    .format(result.score).trim())
                tvDate.text = result.date
                imgItemPhoto.setImageBitmap(bitmap)
            }
            inputStream.close()

            binding.cardView.setOnClickListener{
                val intent = Intent(context, ResultActivity::class.java)
                intent.putExtra("saved_image", result.imagename)
                context.startActivity(intent)
            }

            binding.btnClose.setOnClickListener{
                historyViewModel.delete(result)
                Toast.makeText(context, "Terhapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

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
}
