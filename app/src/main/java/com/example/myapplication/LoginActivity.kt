package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.edit
import com.example.myapplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private var isErrorResponse = false // 에러 응답 여부를 나타내는 변수

    private var isErrorResponse2 = false // 에러 응답 여부를 나타내는 변수

    private lateinit var binding: ActivityLoginBinding
    var isExistBlank = false
    // 앱 상태(로그인 여부) 저장 : 앱 내부 초기 값 설정
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //login false로 지정
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        var loginBtn = findViewById<AppCompatImageButton>(R.id.login)
        var idet = findViewById<EditText>(R.id.email)
        var pwet = findViewById<EditText>(R.id.password)

        val id = binding.email.text.toString()
        val pw = binding.password.text.toString()

        binding.tosignupbtn.setOnClickListener {
            finish()
            val intent = Intent(this, SignupStepActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }

        loginBtn.setOnClickListener {
            if (idet.text.isEmpty() || pwet.text.isEmpty()) {
                isExistBlank = true
                Toast.makeText(this, "로그인 정보를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    sharedPreferences.edit {
                        putBoolean("login", true)
                    }

                    GlobalScope.launch(Dispatchers.IO) {
                        isErrorResponse2 = performLogin(id)
                        if (!isErrorResponse) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "로그인에 성공했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                                val sharedPreference =
                                    getSharedPreferences("Account", Context.MODE_PRIVATE)
                                val editor = sharedPreference.edit()
                                editor.putString("id", id)
                                editor.apply()
                                Log.d("Login", "$id")

                                //로그인 성공 시 메인 화면으로 이동
                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(
                                    R.anim.fromright_toleft,
                                    R.anim.none
                                )
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "학번, 비밀번호를 확인해 주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i("DBtest", ".....ERROR.....!")
                }
            }
        }
    }

    private suspend fun performLogin(id: String): Boolean {
        try {
            var str: String

            Log.d("TestRegisterActivity", "Inside performLogin - Start")

            val url =
                URL("http://192.168.45.129:8081/MJDonor/Android/androidDB.jsp")
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"
            Log.d("TestRegisterActivity", "URL connection established")

            val osw: OutputStream = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(osw, "UTF-8"))

            Log.d("TestRegisterActivity", "HTTP connection setup")

            val sendMsg = "u_id=$id"

            writer.write(sendMsg)
            writer.flush()

            Log.d("TestRegisterActivity", "Data sent to server")

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

                Log.d("TestRegisterActivity", "Data received from server: $receiveMsg")

                if (receiveMsg == "u_id_exists: false") {
                    isErrorResponse2 = true
                } else {
                    isErrorResponse2 = false
                }
            } else {
                Log.d(
                    "TestRegisterActivity",
                    "HTTP connection failed with response code: ${conn.responseCode}"
                )
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("TestRegisterActivity", "IOException: ${e.message}")
            Log.e("TestRegisterActivity", "IOException: $e")
        }

        return false
    }
}
