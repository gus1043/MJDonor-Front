package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDonateinputBinding
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class DonInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonateinputBinding
    private lateinit var currencyFormat: NumberFormat
    private var p_id : Int=0
    private lateinit var title: String
    private lateinit var donLoc: String
    private lateinit var image1: String

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateinputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        p_id = intent.getIntExtra("p_id",1)
        title = intent.getStringExtra("title") ?: "Default Title"
        donLoc = intent.getStringExtra("donLoc") ?: "Default Location"
        image1 = intent.getStringExtra("image1")?: "Default Image"

        currencyFormat = NumberFormat.getCurrencyInstance()

        binding.donationNameDonate.text = title
        binding.companyDonate.text = donLoc

        val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${image1}"
        // 이미지 다운로드 및 설정
        Picasso.get()
            .load("${imageURL}")
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
            .into(binding.selectedimage1)
        // EditText 초기값 설정
        binding.money.setText("0")

        binding.money.textAlignment = EditText.TEXT_ALIGNMENT_TEXT_END

        binding.money.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // 입력된 내용을 가져와서 숫자 형식으로 변환
                val input = s.toString().replace("[^\\d]".toRegex(), "")
                val parsed = if (input.isEmpty()) 0 else input.toInt()
                val formatted = currencyFormat.format(parsed)
                binding.money.removeTextChangedListener(this)
                binding.money.setText(formatted)
                binding.money.setSelection(formatted.length)
                binding.money.addTextChangedListener(this)
            }
        })

        binding.button1.setOnClickListener {
            // 현재 입력된 금액에 1000을 더해서 업데이트
            val currentAmount = binding.money.text.toString().replace("[^\\d]".toRegex(), "").toInt()
            val newAmount = currentAmount + 1000
            binding.money.setText(newAmount.toString())
        }

        binding.button2.setOnClickListener {
            // 현재 입력된 금액에 5000을 더해서 업데이트
            val currentAmount = binding.money.text.toString().replace("[^\\d]".toRegex(), "").toInt()
            val newAmount = currentAmount + 5000
            binding.money.setText(newAmount.toString())
        }

        binding.button3.setOnClickListener {
            // 현재 입력된 금액에 10000을 더해서 업데이트
            val currentAmount = binding.money.text.toString().replace("[^\\d]".toRegex(), "").toInt()
            val newAmount = currentAmount + 10000
            binding.money.setText(newAmount.toString())
        }

        binding.inputdonatebtn.setOnClickListener {
            // 현재 입력된 금액을 가져와서 다음 액티비티로 전달하고, 해당 액티비티로 이동
            val amount = binding.money.text.toString().replace("[^\\d]".toRegex(), "").toInt()
            val intent = Intent(this, DonatePayActivity::class.java)
            intent.putExtra("amount", amount) // 다음 액티비티로 금액 전달
            startActivity(intent) // 다음 액티비티로 이동
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }

        binding.inputdonatebtn.setOnClickListener {
            val isAgree = binding.inputAgree.isChecked
            var isExistBlank = false
            val money = binding.money.text.toString().replace("[^\\d]".toRegex(), "").toInt()
            val nickname = binding.nickname.text.toString()
            if (nickname.isEmpty() ) {
                isExistBlank = true
            }
            if (money == 0 || money < 0) {
                Toast.makeText(this, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if (isExistBlank){
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else if (!isAgree){
                Toast.makeText(this, "약관에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent: Intent = Intent(this, DonatePayActivity::class.java)
                intent.putExtra("p_id", p_id)
                intent.putExtra("image", image1)
                intent.putExtra("money", money) // 다음 액티비티로 기부 금액 전달
                intent.putExtra("nickname", nickname) // 다음 액티비티로 기부자 닉네임 전달
                finish()
                startActivity(intent)
                overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
            }
        }
    }
}
