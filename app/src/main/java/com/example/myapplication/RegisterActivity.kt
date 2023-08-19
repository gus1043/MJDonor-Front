package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.myapplication.databinding.ActivityRegisterBinding
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var currencyFormat: NumberFormat
    val IMAGE_PICK_CODE = 1000 // Request code for image selection
    lateinit var binding: ActivityRegisterBinding
    var isThumbnailVisible = true
    private var firstSelectedBitmap: Bitmap? = null
    private var secondSelectedBitmap: Bitmap? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        binding.endDateEditText.setOnClickListener {
            showDatePickerDialog2()
        }

        //초기 상태
        binding.rightBtn.setVisibility(View.VISIBLE);
        binding.leftBtn.setVisibility(View.GONE);
        binding.selectedimage1.setVisibility(View.VISIBLE);
        binding.selectedimage2.setVisibility(View.GONE);

        binding.selectedimage1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.selectedimage2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.rightBtn.setOnClickListener {
            isThumbnailVisible = false
            binding.selectedimage1.visibility = View.GONE
            binding.selectedimage2.visibility = View.VISIBLE
            binding.rightBtn.visibility = View.GONE
            binding.leftBtn.visibility = View.VISIBLE
            secondSelectedBitmap?.let { binding.selectedimage2.setImageBitmap(it) }
        }

        binding.leftBtn.setOnClickListener {
            isThumbnailVisible = true
            binding.selectedimage1.visibility = View.VISIBLE
            binding.selectedimage2.visibility = View.GONE
            binding.rightBtn.visibility = View.VISIBLE
            binding.leftBtn.visibility = View.GONE
            firstSelectedBitmap?.let { binding.selectedimage1.setImageBitmap(it) }
        }

        binding.inputdonatebtn.setOnClickListener {
            val isAgree = binding.registerAgree.isChecked
            var isExistBlank = false
            val title = binding.registertitle.text.toString()
            val registerDescription = binding.registerDescription.text.toString()
            val goal = binding.goal.text.replace("[^\\d]".toRegex(), "").toInt()

            val startDate = binding.startDateEditText.text.toString()
            val endDate = binding.endDateEditText.text.toString()

            if (title.isEmpty() || registerDescription.isEmpty() || goal == 0 || startDate.isEmpty() || endDate.isEmpty()) {
                isExistBlank = true
            }

            if (firstSelectedBitmap == null || secondSelectedBitmap == null) {
                isExistBlank = true
            }

            if (isExistBlank) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!isAgree) {
                Toast.makeText(this, "약관에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent: Intent = Intent(this, RegisterCardActivity::class.java)
                finish()
                startActivity(intent)
                overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
            }
        }


        currencyFormat = NumberFormat.getCurrencyInstance() // Initialize currencyFormat

        binding.goal.setText("0")
        binding.goal.textAlignment = EditText.TEXT_ALIGNMENT_TEXT_END

        binding.goal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.goal.removeTextChangedListener(this)

                // Remove non-numeric characters and convert to integer
                val input = s.toString().replace("[^\\d]".toRegex(), "")
                val parsed = if (input.isEmpty()) 0 else input.toInt()
                val formatted = currencyFormat.format(parsed)

                binding.goal.setText(formatted)
                binding.goal.setSelection(formatted.length)

                binding.goal.addTextChangedListener(this)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val selectedImage = data?.data

            try {
                val inputStream = contentResolver.openInputStream(selectedImage!!)
                val selectedBitmap = BitmapFactory.decodeStream(inputStream)
                Log.d("bitmap", "$selectedBitmap")
                inputStream?.close()

                if (isThumbnailVisible) {
                    firstSelectedBitmap = selectedBitmap
                    binding.selectedimage1.setImageBitmap(selectedBitmap)
                } else {
                    secondSelectedBitmap = selectedBitmap
                    binding.selectedimage2.setImageBitmap(selectedBitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // 날짜 선택 후 처리할 작업
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                // 선택된 날짜를 처리하거나 표시하는 등의 작업 수행
                binding.startDateEditText.setText(selectedDate) // binding 객체로 뷰 참조
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showDatePickerDialog2() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // 날짜 선택 후 처리할 작업
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                // 선택된 날짜를 처리하거나 표시하는 등의 작업 수행
                binding.endDateEditText.setText(selectedDate) // binding 객체로 뷰 참조
            }, year, month, day)

        datePickerDialog.show()
    }


}

