package com.example.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivitySignupstepBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

class SignupStepActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupstepBinding
    private var position = 0
    val TAG: String = "회원가입"

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView

    private val GET_IMAGE_FOR_IMAGEVIEW1 = 201
    private val GET_IMAGE_FOR_IMAGEVIEW2 = 202
    private val GET_IMAGE_FOR_IMAGEVIEW3 = 203

    val IMAGE_PICK_CODE = 1000
    private var selectedBitmap: Bitmap? = null
    private var selectedImageByteArray: ByteArray? = null
    companion object {
        private const val STEP_1 = 0
        private const val STEP_2 = 1
        private const val STEP_3 = 2
        private const val FINAL_STEP = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupstepBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        imageView1 = findViewById(R.id.imageView1)

        imageView1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }


        binding.stepView.done(false)
        binding.tologinpbtn.setOnClickListener{
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }

        binding.button.setOnClickListener {
            when (position) {
                STEP_1 -> {
                    val studentNum = binding.studentNum.text.toString()
                    val password = binding.password.text.toString()
                    sendPostRequest(studentNum, password)
                    // 에러 응답이 아닐 경우에만 Step 2로 넘어가도록 처리
                    if (!isErrorResponse) {
                        transitionToStep(STEP_2, "Next")
                    }
                }
                STEP_2 -> transitionToStep(STEP_3, "Next")
                STEP_3 -> transitionToStep(FINAL_STEP, "Submit")
                else -> {

                    var isExistBlank = false
                    var isAgree = binding.signupAgree.isChecked
                    var studentNumCorrect = true
                    var emailCorrect = true

                    val email = binding.email.text.toString()
                    val password = binding.password.text.toString()
                    val name = binding.name.text.toString()
                    val studentNumText = binding.studentNum.text.toString()
                    val studentNum = studentNumText.toIntOrNull() ?: "12345678"
                    val walletAdress = binding.walletAddress.text.toString()


                    // 유저가 항목을 다 채우지 않았을 경우
                    if(email.isEmpty() || password.isEmpty() || name.isEmpty()|| studentNumText.isEmpty()|| walletAdress.isEmpty()){
                        isExistBlank = true
                    }

                    //이메일 유효성 체크
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailCorrect = false
                    }

                    //학번 유효성
                    if(!Pattern.matches("(?:\\d{8})", studentNumText))
                    {
                        studentNumCorrect= false
                    }

                    if(!binding.signupAgree.isChecked){
                        isAgree = false
                    } else {
                        isAgree = true
                    }

                    if (!isExistBlank && emailCorrect && studentNumCorrect && isAgree) {
                        // 회원가입 성공 토스트 메세지 띄우기
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        Log.d("bitmapSignup","$email, $name, $password, $walletAdress, $selectedImageByteArray")
                        GlobalScope.launch(Dispatchers.IO) {
                            val result = performSignup(studentNum as Int, email, name, password, walletAdress, selectedImageByteArray)
                            // UI 업데이트 작업 등을 여기에 추가할 수 있습니다.
                            if (result != null) {
                                runOnUiThread {
                                    //로그인 성공 시 메인 화면으로 이동
                                    val intent = Intent(this@SignupStepActivity, LoginActivity::class.java)
                                    finish()
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
                                }
                            }
                        }
                    }
                    else{
                        // 상태에 따라 다른 다이얼로그 띄워주기
                        if(isExistBlank){   // 작성 안한 항목이 있을 경우
                            dialog("blank")
                        }
                        else if(!emailCorrect){ // 입력한 비밀번호가 다를 경우
                            dialog("emailCorrect")
                        }
                        else if(!isAgree){ // 이용약관 동의 안한 경우
                            dialog("Agree")
                        }
                    }
                }
            }
        }
    }

    private suspend fun performSignup(studentNum: Int, email: String, name: String, password: String, walletAdress: String, photobytearray: ByteArray?): String? {
        try {
            val url = URL("http://192.168.0.101:8081/MJDonor/Android/performSignup.jsp")
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"

            val osw: OutputStream = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(osw, "UTF-8"))

            val sendMsg = "u_id=$studentNum&email=$email&name=$name&password=$password&wallet=$walletAdress&photo=${photobytearray}"
            Log.d("bitmapPerform", "u_id=$studentNum&email=$email&name=$name&password=$password&wallet=$walletAdress&photo=${photobytearray}")
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
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("TestRegisterActivity", "IOException: ${e.message}")
            Log.e("TestRegisterActivity", "IOException: $e")
        }

        return null
    }


    override fun onBackPressed() {
        when (position) {
            STEP_1 -> super.onBackPressed()
            STEP_2 -> transitionToStep(STEP_1, "Next")
            STEP_3 -> transitionToStep(STEP_2, "Next")
            else -> transitionToStep(STEP_3, "Next")
        }
    }

    private fun transitionToStep(nextPosition: Int, buttonText: String) {
        when (position) {
            STEP_1 -> binding.STEP1.visibility = View.GONE
            STEP_2 -> binding.STEP2.visibility = View.GONE
            STEP_3 -> binding.STEP3.visibility = View.GONE
            FINAL_STEP -> binding.FINALSTEP.visibility = View.GONE
        }
        position = nextPosition
        binding.stepView.done(false)
        binding.stepView.go(position, true)
        binding.button.text = buttonText
        when (position) {
            STEP_1 -> binding.STEP1.visibility = View.VISIBLE
            STEP_2 -> binding.STEP2.visibility = View.VISIBLE
            STEP_3 -> binding.STEP3.visibility = View.VISIBLE
            FINAL_STEP -> binding.FINALSTEP.visibility = View.VISIBLE
        }
    }
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 정확히 작성해주세요")
        }
        else if(type.equals("studentNumCorrect")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("명지대 통합로그인 아이디, 비밀번호를 입력해주세요!")
        }
        else if(type.equals("emailCorrect")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("이메일 형식을 맞춰주세요")
        }
        else if(type.equals("Agree")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("이용약관에 동의해주세요")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val selectedImage = data?.data

            try {
                val inputStream = contentResolver.openInputStream(selectedImage!!)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)

                inputStream?.close()

                val byteArrayOutputStream = ByteArrayOutputStream()
                selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                selectedImageByteArray = byteArrayOutputStream.toByteArray()  // Store the byte array
                Log.d("bitmapThumb", "$selectedImageByteArray")
                binding.imageView1.setImageBitmap(selectedBitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var isErrorResponse = false // 에러 응답 여부를 나타내는 변수

    private fun sendPostRequest(studentNum: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("https://sso1.mju.ac.kr/mju/userCheck.do")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true

                val postData = "id=$studentNum&passwrd=$password"
                val os: OutputStream = conn.outputStream
                val writer = OutputStreamWriter(os, "UTF-8")
                writer.write(postData)
                writer.flush()
                writer.close()
                os.close()

                val responseCode = conn.responseCode
                val response = conn.inputStream.bufferedReader().use { it.readText() }

                val jsonResponse = JSONObject(response)
                val errorCode = jsonResponse.getString("error")
                Log.d("PLEASE", errorCode) // Log the error code

                if (errorCode == "VL-2100") {
                    // Handle "VL-2100" error response
                    runOnUiThread {
                        dialog("studentNumCorrect")
                        isErrorResponse = true // Set error flag
                    }
                } else if (errorCode == "0000") {
                    // Handle "0000" success response
                    runOnUiThread {
                        isErrorResponse = false // Set success flag
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}