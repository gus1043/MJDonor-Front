package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonationselectionBinding

class DonSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonationselectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selecteddonation.setOnClickListener {
            openDonationTree()
        }

        binding.selectdonatebtn.setOnClickListener {
            val intent: Intent = Intent(this, DonInputActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }
    }

    private fun openDonationTree() {
        val fragment = DonationTree()
        fragment.show(supportFragmentManager, "donationTreeDialog")
    }
}
