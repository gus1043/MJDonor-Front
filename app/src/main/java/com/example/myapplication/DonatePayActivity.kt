package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonatepayBinding

class DonatePayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonatepayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatepayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.payButton.setOnClickListener {
            val intent: Intent = Intent(this, DonatedCardActivity::class.java)
            startActivity(intent)
        }
    }
}

