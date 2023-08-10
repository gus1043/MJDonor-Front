package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonateinputBinding
import com.example.myapplication.databinding.ActivityDonationselectionBinding
import com.example.myapplication.databinding.ActivityMainBinding

class DonInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonateinputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inputdonatebtn.setOnClickListener {
//            val intent: Intent = Intent(this, DonInputActivity::class.java)
//            startActivity(intent)
        }
    }
}