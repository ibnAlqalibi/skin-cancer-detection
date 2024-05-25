package com.dicoding.asclepius.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var currentSavedImage: String

    private val cropperContract = object: ActivityResultContract<List<Uri>, Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inUri = input[0]
            val outUri = input[1]
            return UCrop.of(inUri, outUri)
                    .withAspectRatio(5f, 5f)
                    .withMaxResultSize(800, 800)
                    .getIntent(context)

        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            if (intent != null){
                return UCrop.getOutput(intent)!!
            }
            return currentImageUri!!
        }

    }

    private val crop = registerForActivityResult(cropperContract){ uri ->

        currentSavedImage = "croppedImage${(0..999999).random()}.jpg"
        binding.previewImageView.setImageURI(uri)
        saveFileToInternalStorage(this, uri, currentSavedImage)
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri: Uri? ->
        val rnds = (0..999999).random()
        val outputUri = File(filesDir, "croppedImage$rnds.jpg").toUri()

        if (uri != null) {
            val listUri = listOf(uri, outputUri)
            currentImageUri = uri
            crop.launch(listUri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }

    }

    private fun saveFileToInternalStorage(context: Context, uri: Uri, filename: String) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return
            val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.close()
            inputStream.close()
            Log.d("SaveFile", "File saved successfully to internal storage: $filename")
        } catch (e: Exception) {
            Log.e("SaveFile", "Error saving file: $e")
        }

        val savedFileContent = try {
            val inputStream = openFileInput(filename)
            inputStream.close()
        } catch (e: Exception) {
            null
        }

        if (savedFileContent != null) {
            showToast("File saved successfully to internal storage: $filename")
        } else {
            Log.w("ReadFile", "Failed to read saved file: $filename")
        }


    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        with(binding){
            btnHistory.setOnClickListener{
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
            analyzeButton.setOnClickListener { moveToResult() }
            galleryButton.setOnClickListener { startGallery() }
            cameraButton.setOnClickListener { startCameraX() }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            val rnds = (0..999999).random()
            val outputUri = File(filesDir, "croppedImage$rnds.jpg").toUri()

            if (currentImageUri != null) {
                val listUri = listOf(currentImageUri.toString().toUri(), outputUri)
                crop.launch(listUri)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun moveToResult() {
        if (currentImageUri == null){
            showToast("Pilih gambar terlebih dahlu!")
        }else{
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("uri", currentImageUri.toString())
            intent.putExtra("saved_image", currentSavedImage)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}