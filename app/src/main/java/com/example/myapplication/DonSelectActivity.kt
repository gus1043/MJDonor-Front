package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonationselectionBinding
import com.example.myapplication.databinding.ActivityMainBinding

class DonSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonationselectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectdonatebtn.setOnClickListener {
            val intent: Intent = Intent(this, DonInputActivity::class.java)
            startActivity(intent)
        }
    }
}