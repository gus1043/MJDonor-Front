package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonatedCardBinding

class DonatedCardActivity : AppCompatActivity() {

    override fun onBackPressed() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
        overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonatedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toMypage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selectedFragment", "fragment3")
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }
    }
}
