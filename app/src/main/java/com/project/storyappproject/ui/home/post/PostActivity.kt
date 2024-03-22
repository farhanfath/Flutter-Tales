package com.project.storyappproject.ui.home.post

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.project.storyappproject.R
import com.project.storyappproject.createCustomTempFile
import com.project.storyappproject.databinding.ActivityPostBinding
import com.project.storyappproject.ui.customview.CustomAlert
import com.project.storyappproject.ui.customview.CustomSuccessAlert
import com.project.storyappproject.ui.home.post.camerax.CameraActivity
import com.project.storyappproject.ui.home.post.camerax.CameraActivity.Companion.CAMERAX_RESULT
import com.project.storyappproject.uriToFile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private val postViewModel : PostViewModel by viewModels()
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        postViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        postViewModel.isError.observe(this) {
            Log.d("test", "Gagal upload")
        }

        checkPermission()

        startGallery()
        startCamera()
        startCameraX()
        postStoryButton()
        backButton()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_PERMISSIONS) {
            if (!permissionGranted()) {
                Toast.makeText(this@PostActivity, R.string.no_access, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun permissionGranted() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission() {
        if (!permissionGranted()) {
            ActivityCompat.requestPermissions(
                this@PostActivity,
                PERMISSIONS,
                CODE_PERMISSIONS
            )
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.postLayout.storyPictPreview.setImageBitmap(result)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        binding.postLayout.camBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            createCustomTempFile(applicationContext).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@PostActivity,
                    "com.project.storyappproject.mycamera",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@PostActivity)
            getFile = myFile
            binding.postLayout.storyPictPreview.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        binding.postLayout.galleryBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val picImage = Intent.createChooser(intent, "Select An Image")
            launcherIntentGallery.launch(picImage)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val imageUriString = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
            val imageUri = imageUriString?.toUri() as Uri
            val myFile = uriToFile(imageUri, this)
            getFile = myFile

            binding.postLayout.storyPictPreview.setImageURI(imageUri)
        }
    }
    private fun startCameraX() {
        binding.postLayout.camxBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }
    }

    private fun postStoryButton() {
        binding.postLayout.postBtn.setOnClickListener {
            val description = binding.postLayout.descEt.text.toString()
            if (!TextUtils.isEmpty(description) && getFile != null) {
                lifecycleScope.launch {
                    showProgressBar()
                    try {
                        postViewModel.postStory(getFile!!, description)
                        Log.d("testPost", "isi postingan : $description , $longitude, $latitude")
                        showSuccessAlert()
                    } catch (e: Exception) {
                        showErrorAlert()
                    } finally {
                        hideProgressBar()
                    }
                }
            } else {
                CustomAlert(this, R.string.errorPost, R.drawable.alert_post_img).show()
            }
        }
    }

    private fun showProgressBar() {
        binding.postLayout.root.visibility = View.INVISIBLE
        binding.loadingLayout.root.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loadingLayout.root.visibility = View.INVISIBLE
    }

    private fun showSuccessAlert() {
        val customSuccessAlert = CustomSuccessAlert(this, R.string.successPost, R.drawable.success_post_alert_img) {
            finish()
            binding.postLayout.root.visibility = View.VISIBLE
        }
        customSuccessAlert.show()
    }

    private fun showErrorAlert() {
        CustomAlert(this, R.string.errorPost, R.drawable.alert_post_img).show()
    }


    private fun backButton() {
        binding.postLayout.backHome.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        val PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        const val CODE_PERMISSIONS = 10
    }
}