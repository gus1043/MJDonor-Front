package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonationselectionBinding

class DonSelectActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var donLoc: String
    private var imageResId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonationselectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트로 전달된 값 받기
        title = intent.getStringExtra("title") ?: "Default Title"
        donLoc = intent.getStringExtra("donLoc") ?: "Default Location"

        binding.selecteddonation.setOnClickListener {
            openDonationTree()
        }

        binding.selectdonatebtn.setOnClickListener {
            val intent: Intent = Intent(this, DonInputActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }

        // 받은 값을 뷰에 설정
        binding.selectedtitle.text = title
        binding.company.text = donLoc
    }

    private fun openDonationTree() {
        val fragment = DonationTree()
        fragment.show(supportFragmentManager, "donationTreeDialog")
    }
}

