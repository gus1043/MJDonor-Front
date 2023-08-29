package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonatedCardBinding
import com.example.myapplication.databinding.ActivityDonatepayBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class DonatedCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonatedCardBinding
    private var p_id : Int=0
    private lateinit var nickname: String
    private lateinit var duedate: String
    private var point : Int=0
    override fun onBackPressed() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
        overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        point = intent.getIntExtra("point",1)
        p_id = intent.getIntExtra("p_id",1)
        nickname = intent.getStringExtra("nick") ?: "Default nickname"
        duedate = intent.getStringExtra("duedate") ?: "Default duedate"
        // totalDonation 데이터 파싱
        val virtualAccountUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/donationInfo.jsp?p_id=${p_id}&nickname=${nickname}&point=${point}"
        val virtualAccountUrlConnection = URL(virtualAccountUrl).openConnection() as HttpURLConnection

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val virtualAccountInputStream = virtualAccountUrlConnection.inputStream
                val virtualAccountContent =
                    virtualAccountInputStream.bufferedReader().use { it.readText() }

                // 필요한 데이터 추출
                val virtualAccount =
                    virtualAccountContent.substringAfter("Account: ").trim()
                withContext(Dispatchers.Main) {
                    binding.virtualAccount.text = virtualAccount
                    binding.depositDeadline.text = duedate
                }

            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
                virtualAccountUrlConnection.disconnect()
            }
        }

        binding.toMypage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selectedFragment", "fragment3")
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }
    }
}
