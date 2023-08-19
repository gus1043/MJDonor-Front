package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var currencyFormat: NumberFormat
    val IMAGE_PICK_CODE = 1000 // Request code for image selection
    lateinit var binding: ActivityRegisterBinding
    var isThumbnailVisible = true
    private var firstSelectedBitmap: Bitmap? = null
    private var secondSelectedBitmap: Bitmap? = null

    private lateinit var sharedPreferences: SharedPreferences

    private val typeMap = mapOf(
        "저소득층" to 0,
        "한부모가정" to 1,
        "환경" to 2,
        "글로벌" to 3
    )

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

        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE)

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
            val type = binding.type.selectedItem.toString()

            val u_idString = sharedPreferences.getString("id", "")
            val u_id = u_idString?.toIntOrNull() ?: 0

            val o_id = typeMap[type] ?: -1  // type에 해당하는 숫자 값을 가져옴
            if (o_id == -1) {
                Toast.makeText(this, "잘못된 타입 값입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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


                GlobalScope.launch(Dispatchers.IO) {


                    val startdateString = startDate // Your date string
                    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.US)
                    val startparsedDate = dateFormat.parse(startdateString)

                    val enddateString = endDate // Your date string
                    val endparsedDate = dateFormat.parse(enddateString)


                    val result = firstSelectedBitmap?.let { it1 ->
                        secondSelectedBitmap?.let { it2 ->
                            performRegister(title, registerDescription,
                                goal, startparsedDate as Date, endparsedDate as Date,
                                it1,
                                it2,
                                type,
                                o_id,
                                u_id
                            )
                        }
                    }
                    // UI 업데이트 작업 등을 여기에 추가할 수 있습니다.
                    if (result != null) {
                        runOnUiThread {
                            //로그인 성공 시 메인 화면으로 이동
                            val intent: Intent = Intent(this@RegisterActivity, RegisterCardActivity::class.java)
                            finish()
                            startActivity(intent)
                            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)

                            Log.d("RegisterActivity", "Title: $title")
                            Log.d("RegisterActivity", "Register Description: $registerDescription")
                            Log.d("RegisterActivity", "Goal: $goal")
                            Log.d("RegisterActivity", "First Selected Image: $firstSelectedBitmap")
                            Log.d("RegisterActivity", "Second Selected Image: $secondSelectedBitmap")
                            Log.d("RegisterActivity", "Type: $type")
                            Log.d("RegisterActivity", "o_id: $o_id")
                            Log.d("RegisterActivity", "u_id: $u_id")
                        }
                    }
                }
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

    private suspend fun performRegister(title: String, registerDescription: String, goal: Int, startDate: Date, endDate: Date, firstSelectedImage: Bitmap, secondSelectedImage: Bitmap, type: String, o_id: Int, u_id: Int): String? {
        try {

            fun formatDate(date: Date): String {
                val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.US)
                return dateFormat.format(date)
            }

            val url = URL("http://192.168.0.101:8081/MJDonor/Android/performRegister.jsp")
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"

            val osw: OutputStream = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(osw, "UTF-8"))

            val sendMsg = "name=$title&description=$registerDescription&target_point=$goal&start_date=${formatDate(startDate)}&end_date=${formatDate(endDate)}&image1=${firstSelectedImage.toString()}&image2=${secondSelectedImage.toString()}&category=$type&ORGANIZATION_ID=$o_id&REGISTRANT_ID=$u_id"

            writer.write(sendMsg)
            writer.flush()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val tmp = InputStreamReader(conn.inputStream, "UTF-8")
                val reader = BufferedReader(tmp)
                val buffer = StringBuffer()

                var str: String? = null
                while (reader.readLine().also { str = it } != null) {
                    str?.let {
                        buffer.append(it)
                    }
                }
                val receiveMsg = buffer.toString()
                return receiveMsg
            } else {
                Log.d("TestRegisterActivity", "HTTP connection failed with response code: ${conn.responseCode}")
            }

            Log.d("RegisterActivity", "Title: $title")
            Log.d("RegisterActivity", "Register Description: $registerDescription")
            Log.d("RegisterActivity", "Goal: $goal")
            Log.d("RegisterActivity", "Start Date: ${formatDate(startDate)}")
            Log.d("RegisterActivity", "End Date: ${formatDate(endDate)}")
            Log.d("RegisterActivity", "First Selected Image: $firstSelectedImage")
            Log.d("RegisterActivity", "Second Selected Image: $secondSelectedImage")
            Log.d("RegisterActivity", "Type: $type")
            Log.d("RegisterActivity", "o_id: $o_id")
            Log.d("RegisterActivity", "u_id: $u_id")

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("TestRegisterActivity", "IOException: ${e.message}")
            Log.e("TestRegisterActivity", "IOException: $e")
        }
        return null
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
                val selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", selectedYear % 100, selectedMonth + 1, selectedDay)
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
                val selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", selectedYear % 100, selectedMonth + 1, selectedDay)
                // 선택된 날짜를 처리하거나 표시하는 등의 작업 수행
                binding.endDateEditText.setText(selectedDate) // binding 객체로 뷰 참조
            }, year, month, day)

        datePickerDialog.show()
    }


}

