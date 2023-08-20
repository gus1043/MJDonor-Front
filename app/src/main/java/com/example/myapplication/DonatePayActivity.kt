package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonatepayBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class DonatePayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonatepayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatepayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전 액티비티(DonInputActivity)에서 넘어온 데이터 추출
        val amount = intent.getIntExtra("donationAmount", 0) // 인텐트에서 금액 데이터 추출
        val accountNumber = intent.getStringExtra("accountnumber")
        val bank = intent.getStringExtra("bank")
        val mes = intent.getStringExtra("mes")
        val nickname = intent.getStringExtra("donorNickname")

        binding.payButton.setOnClickListener {
            val point = 10000
            val email = "example@email.com"
            val nick = "JohnDoe"
            val project = "ProjectXYZ"
            val due = "2023-08-31"
            val rbank = "국민"
            val r_a = "1234567890"

            val jsonObject = JSONObject()
            jsonObject.put("point", point)
            jsonObject.put("email", email)
            jsonObject.put("nick", nick)
            jsonObject.put("project", project)
            jsonObject.put("due", due)
            jsonObject.put("rbank", rbank)
            jsonObject.put("r_a", r_a)

            val jsonString = jsonObject.toString()

            val apiUrl = "http://192.168.0.101:8081/MJDonor/Android/virtual_account.jsp"

            val client = OkHttpClient()
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonString)
            val request = Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    // Handle the response here
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    // Handle failure here
                }
            })
        }
    }
}
