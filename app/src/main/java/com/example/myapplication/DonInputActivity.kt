package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonateinputBinding
import java.text.NumberFormat

class DonInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonateinputBinding
    private lateinit var currencyFormat: NumberFormat

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateinputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currencyFormat = NumberFormat.getCurrencyInstance()

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

        binding.inputdonatebtn.setOnClickListener {
            // TODO: 입력된 금액 처리
            // 예: 입력된 금액을 다음 단계로 전달하거나 로직을 수행
        }

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
        }
    }
}
