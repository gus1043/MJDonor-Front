package com.example.myapplication

import android.annotation.SuppressLint
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
import kotlinx.coroutines.withContext
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
    private lateinit var title: String
    private lateinit var donLoc: String
    private lateinit var image1: String
    private var money : Int=0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatepayBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE)
        setContentView(binding.root)

        // 이전 액티비티(DonInputActivity)에서 넘어온 데이터 추출
        money = intent.getIntExtra("money",1)
        donLoc =  intent.getStringExtra("donLoc") ?: "Default donLoc"
        title =  intent.getStringExtra("title") ?: "Default title"
        nickname = intent.getStringExtra("nickname") ?: "Default nickname"
        image1 = intent.getStringExtra("image") ?: "Default image"
        p_id = intent.getIntExtra("p_id",1)
        Log.d("Donatepay p_id", p_id.toString())
        Log.d("pay", image1)
        val id = sharedPreferences.getString("id", "")

        val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${image1}"
        Log.d("pay", imageURL)
        // 이미지 다운로드 및 설정
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${image1}"
                    // 이미지 다운로드 및 설정
                    Picasso.get()
                        .load("${imageURL}")
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
                        .into(binding.selectedimage1)
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }
        }

        binding.paymentAmount.setText(money.toString() + "원")
        binding.donationName.text = title
        binding.company.text = donLoc

        binding.payButton.setOnClickListener {

            val rbank = binding.bank.selectedItem.toString()
            val r_a = binding.accountnumber.text.toString()
            val msg = binding.mes.text.toString()

            val u_id = id?.toInt()
            val point = money
            val nick = nickname
            val p_id = p_id

            val realDate = Date() // Current Date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dueDate = dateFormat.format(realDate)

            Log.d("DonatePayActivity", "u_id: $u_id")
            Log.d("DonatePayActivity", "point: $point")
            Log.d("DonatePayActivity", "nick: $nick")
            Log.d("DonatePayActivity", "msg: $msg")
            Log.d("DonatePayActivity", "p_id: $p_id")
            Log.d("DonatePayActivity", "dueDate: $dueDate")
            Log.d("DonatePayActivity", "rbank: $rbank")
            Log.d("DonatePayActivity", "r_a: $r_a")

            GlobalScope.launch(Dispatchers.IO) {

                val calendar = Calendar.getInstance()
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val dueDate = calendar.time

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
                        intent.putExtra("p_id", p_id)
                        intent.putExtra("nick", nick)
                        intent.putExtra("point", point)
                        intent.putExtra("duedate", dueDate.toString())
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

                val url = URL("http://jsp.mjdonor.kro.kr:8888/webapp/Android/virtual_account.jsp")
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
