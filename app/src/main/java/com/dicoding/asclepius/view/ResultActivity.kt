package com.dicoding.asclepius.view

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.ResultEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.DateHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodels.HistoryViewModel
import com.dicoding.asclepius.viewmodels.HistoryViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import kotlin.properties.Delegates

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var label: String
    private var score by Delegates.notNull<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showImage()
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun showImage() {
        val imageName = intent.getStringExtra("saved_image")
        startClassifier()
        imageClassifierHelper.classifyImage(imageName)

        val inputStream = openFileInput(imageName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        binding.resultImage.setImageBitmap(bitmap)

        val historyViewModel = obtainViewModel(this@ResultActivity)
        historyViewModel.getResultByImageName(imageName).observe(this){
            if (it.isEmpty()){
                val id = "img${(0..999999).random()}"
                val resultEntity = ResultEntity(id = id, imagename = imageName, label = label, date = DateHelper.getCurrentDate(), score = score)
                historyViewModel.insert(resultEntity)
            }
        }
    }

    private fun startClassifier() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        label = it.label
                                        score = it.score
                                        "${label} " + NumberFormat.getPercentInstance()
                                            .format(score).trim()
                                    }
                                binding.resultText.text = displayResult
                                binding.timeText.text =getString(R.string.waktu_analisis_ms, inferenceTime)

                            } else {
                                binding.resultText.text = ""
                                binding.timeText.text = ""
                            }
                        }
                    }
                }
            }
        )
    }
}