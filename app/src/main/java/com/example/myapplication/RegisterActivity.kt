package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding
import java.io.File
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {

    val IMAGE_PICK_CODE = 1000 // Request code for image selection
    lateinit var binding: ActivityRegisterBinding
    var isThumbnailVisible = true
    var firstSelectedImage: Uri? = null
    var secondSelectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //초기 상태
        binding.rightBtn.setVisibility(View.VISIBLE);
        binding.leftBtn.setVisibility(View.GONE);
        binding.selectedimage1.setVisibility(View.VISIBLE);
        binding.selectedimage2.setVisibility(View.GONE);

        binding.selectedimage1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.selectedimage2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.rightBtn.setOnClickListener {
            isThumbnailVisible = false
            binding.selectedimage1.visibility = View.GONE
            binding.selectedimage2.visibility = View.VISIBLE
            binding.rightBtn.visibility = View.GONE
            binding.leftBtn.visibility = View.VISIBLE
            secondSelectedImage?.let { binding.selectedimage2.setImageURI(it) }
        }

        binding.leftBtn.setOnClickListener {
            isThumbnailVisible = true
            binding.selectedimage1.visibility = View.VISIBLE
            binding.selectedimage2.visibility = View.GONE
            binding.rightBtn.visibility = View.VISIBLE
            binding.leftBtn.visibility = View.GONE
            firstSelectedImage?.let { binding.selectedimage1.setImageURI(it) }
        }

        binding.inputdonatebtn.setOnClickListener {
            val intent: Intent = Intent(this, RegisterCardActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val selectedImage = data?.data

            if (isThumbnailVisible) {
                firstSelectedImage = selectedImage
                binding.selectedimage1.setImageURI(selectedImage)
            } else {
                secondSelectedImage = selectedImage
                binding.selectedimage2.setImageURI(selectedImage)
            }
        }
    }
}

