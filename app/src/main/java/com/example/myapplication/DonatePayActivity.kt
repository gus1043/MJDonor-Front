package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonatepayBinding

class DonatePayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonatepayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatepayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: 해당 액티비티의 기능을 구현
    }
}
