package com.ibnAlqalibi.SimpleSkinCancerDetection.view

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ibnAlqalibi.SimpleSkinCancerDetection.R
import com.ibnAlqalibi.SimpleSkinCancerDetection.database.ResultEntity
import com.ibnAlqalibi.SimpleSkinCancerDetection.databinding.ActivityResultBinding
import com.ibnAlqalibi.SimpleSkinCancerDetection.helper.DateHelper
import com.ibnAlqalibi.SimpleSkinCancerDetection.helper.ImageClassifierHelper
import com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels.HistoryViewModel
import com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels.HistoryViewModelFactory
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
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
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