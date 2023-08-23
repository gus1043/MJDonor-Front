package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDonatepayBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class DonatePayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonatepayBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var p_id : Int=0
    private lateinit var nickname: String
    private lateinit var money: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatepayBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE)
        setContentView(binding.root)

        // 이전 액티비티(DonInputActivity)에서 넘어온 데이터 추출
        money = intent.getStringExtra("money") ?: "Default money"
        nickname = intent.getStringExtra("nickname") ?: "Default nickname"
        p_id = intent.getIntExtra("p_id",1)
        Log.d("Donatepay p_id", p_id.toString())

        val id = sharedPreferences.getString("id", "")
        val image1 = intent.getStringExtra("image1")?: "Default Image"

        val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${image1}"
        // 이미지 다운로드 및 설정
        Picasso.get()
            .load("${imageURL}")
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
            .into(binding.selectedimage1)

        binding.paymentAmount.setText(money)

        binding.payButton.setOnClickListener {

            val rbank = binding.bank.selectedItem.toString()
            val r_a = binding.accountnumber.text.toString()
            val msg = binding.mes.text.toString()

            val u_id = id?.toInt()
            val point = money.toInt()
            val nick = nickname
            val p_id = p_id

            val dueDateStr = "2023-04-05"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dueDate: Date = dateFormat.parse(dueDateStr)

            Log.d("DonatePayActivity", "u_id: $u_id")
            Log.d("DonatePayActivity", "point: $point")
            Log.d("DonatePayActivity", "nick: $nick")
            Log.d("DonatePayActivity", "msg: $msg")
            Log.d("DonatePayActivity", "p_id: $p_id")
            Log.d("DonatePayActivity", "dueDate: ${formatDate(dueDate)}")
            Log.d("DonatePayActivity", "rbank: $rbank")
            Log.d("DonatePayActivity", "r_a: $r_a")

            GlobalScope.launch(Dispatchers.IO) {

                val result =
                    nick?.let { it1 ->
                        if (u_id != null) {
                            performVirtualAccount(
                                u_id, point, it1, msg,
                                p_id, dueDate, rbank,
                                r_a
                            )
                        } else{
                            performVirtualAccount(
                                0, point, it1, msg,
                                p_id, dueDate, rbank,
                                r_a
                            )
                        }
                    }

                // UI 업데이트 작업 등을 여기에 추가할 수 있습니다.
                if (result != null) {
                    runOnUiThread {
                        val intent: Intent =
                            Intent(this@DonatePayActivity, DonatedCardActivity::class.java)
                        finish()
                        startActivity(intent)
                        overridePendingTransition(R.anim.fromright_toleft, R.anim.none)

                    }
                }
            }
        }
    }

        private suspend fun performVirtualAccount(u_id: Int, point: Int, nick: String, msg: String, p_id: Int, dueDate: Date, rbank: String, r_a: String
        ): String? {
            try {

                fun formatDate(date: String): String {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    return dateFormat.format(date)
                }

                val url = URL("http://192.168.0.102:8081/MJDonor/Android/virtual_account.jsp")
                val conn = url.openConnection() as HttpURLConnection
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                conn.requestMethod = "POST"

                val osw: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(osw, "UTF-8"))

                val sendMsg = "u_id=$u_id&point=$point&nick=$nick&msg=$msg&p_id=$p_id&dueDate=${formatDate(dueDate)}&rbank=$rbank&r_a=$r_a"

                writer.write(sendMsg)
                writer.flush()

                Log.d("TestDonatePayActivity", sendMsg)

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

                    Log.d("TestDonatePayActivity", receiveMsg)
                    return receiveMsg
                } else {
                    Log.d("TestDonatePayActivity", "HTTP connection failed with response code: ${conn.responseCode}")

                }

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("TestDonatePayActivity", "IOException: ${e.message}")
                Log.e("TestDonatePayActivity", "IOException: $e")
            }
            return null
        }
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(date)
    }
