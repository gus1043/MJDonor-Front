package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        var loginBtn = findViewById<AppCompatImageButton>(R.id.login)
        var idet = findViewById<EditText>(R.id.email)
        var pwet = findViewById<EditText>(R.id.password)

        loginBtn.setOnClickListener {
            try {
                Toast.makeText(this@LoginActivity, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                val id = idet.text.toString()
                val pw = pwet.text.toString()

                GlobalScope.launch(Dispatchers.IO) {
                    val result = performLogin(id, pw)
                    // UI 업데이트 작업 등을 여기에 추가할 수 있습니다.
                    if (result != null) {
                        runOnUiThread {
                            // 예시: 로그인 성공 시 메인 화면으로 이동
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("DBtest", ".....ERROR.....!")
            }
        }
    }

    private suspend fun performLogin(id: String, pw: String): String? {
        try {
            var str: String

            Log.d("TestRegisterActivity", "Inside performLogin - Start")

            val url = URL("http://ip/DB_Connect/Android/androidDB.jsp") // 헤로쿠 배포해야 할 듯
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"
            Log.d("TestRegisterActivity", "URL connection established")

            val osw: OutputStream = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(osw, "UTF-8"))

            Log.d("TestRegisterActivity", "HTTP connection setup")

            val sendMsg = "id=$id&pw=$pw"

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
}
