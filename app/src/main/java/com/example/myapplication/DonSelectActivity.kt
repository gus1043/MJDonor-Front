package com.example.myapplication

import DonorAdapter
import DonorItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDonationselectionBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DonSelectActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var donLoc: String
    private lateinit var description: String
    private lateinit var image1: String
    private lateinit var image2: String
    private var p_id : Int=0
    private lateinit var enddate : String
    private lateinit var goal :String
    private lateinit var current: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonationselectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트로 전달된 값 받기
        title = intent.getStringExtra("title") ?: "Default Title"
        donLoc = intent.getStringExtra("donLoc") ?: "Default Location"
        image1 = intent.getStringExtra("image1")?: "Default Image"
        image2 = intent.getStringExtra("image2")?: "Default Image"
        p_id = intent.getIntExtra("p_id",1)
        goal = intent.getStringExtra("goal")?: "Default Image"
        enddate = intent.getStringExtra("enddate")?: "Default Image"
        current = intent.getStringExtra("current")?: "Default Image"
        description = intent.getStringExtra("description") ?: "Default Description"

        val endDateDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(enddate)
        val formattedGoal = NumberFormat.getNumberInstance(Locale.getDefault()).format(goal.toDouble())
        val formattedCurrent = NumberFormat.getNumberInstance(Locale.getDefault()).format(current.toDouble())

        binding.selecteddonation.setOnClickListener {
            openDonationTree()
        }

        binding.selectdonatebtn.setOnClickListener {
            val intent: Intent = Intent(this, DonInputActivity::class.java)
            intent.putExtra("p_id", p_id)
            intent.putExtra("title", title)
            intent.putExtra("donLoc", donLoc)
            intent.putExtra("image1", image1)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }
        Log.d("donselect please p_id", p_id.toString())
        Log.d("donselect please title", title)
        Log.d("donselect please", goal)
        Log.d("donselect please", enddate)
        val DonorCnturl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/donationCount.jsp?p_id=${p_id}"
        Log.d("please DonorCnturl", DonorCnturl)
        val url = URL(DonorCnturl)
        val connection = url.openConnection() as HttpURLConnection


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = connection.inputStream
                val content = inputStream.bufferedReader().use { it.readText() }

                // 파싱된 결과에서 필요한 데이터 추출
                val cnt= content.substringAfter("Donation Count: ").trim()
                Log.d("donselect CNt", cnt)
                Log.d("donselect CNt", "$cnt 기부자")

                withContext(Dispatchers.Main) {
                    binding.numPeople.text ="$cnt 기부자"
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }
        }


        // 받은 값을 뷰에 설정
        binding.selectedtitle.text = title
        binding.company.text = donLoc

        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val imageURL = "http://jsp.mjdonor.kro.kr:9999/webapp/Storage/download.jsp?filename=${image1}"
                    // 이미지 다운로드 및 설정
                    Picasso.get()
                        .load("${imageURL}")
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo) // 에러 대체 이미지를 지정해주세요
                        .into(binding.selecteddonation)
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
            }
        binding.donationContent.text = description
        binding.goal.text = "$formattedGoal 원"
        binding.enddate.text = "$enddate 까지"
        binding.current.text="$formattedCurrent 원"

        val DonorlistUrl = "http://jsp.mjdonor.kro.kr:8888/webapp/Android/donorPhoto.jsp?p_id=${p_id}"
        Log.d("please D_url", DonorlistUrl)
        val DonorlistUrlConnection = URL(DonorlistUrl).openConnection() as HttpURLConnection
        val ProjectDonors = mutableListOf<DonorItem>()

        val recyclerViewmain = binding.recyclerViewmain
        recyclerViewmain.layoutManager = LinearLayoutManager(this@DonSelectActivity, LinearLayoutManager.HORIZONTAL, false)

        GlobalScope.launch(Dispatchers.IO) {
            try {

                val DonorlistInputStream = DonorlistUrlConnection.inputStream
                val DonorlistContent = DonorlistInputStream.bufferedReader().use { it.readText().trim() }
                Log.d("please DonorlistContent", "Lines: $DonorlistContent")
                val lines = DonorlistContent.split("\n","<br>")
                Log.d("donselect", "Lines: $lines")

                for (line in lines) {
                    if (line.isNotEmpty()) {
                        Log.d("donselect", "Line: $line")
                        val U_id = line.substringAfter("User ID: ").trim()
                        val Photo = line.substringAfter("Photo: ").trim()
                        val Projectdonor = DonorItem(
                            Photo, p_id
                        )
                        ProjectDonors.add(Projectdonor)
                        Log.d("please user", ProjectDonors.toString())
                    }
                }

                withContext(Dispatchers.Main) {
                    val adapter = DonorAdapter(this@DonSelectActivity, ProjectDonors)
                    Log.d("please", ProjectDonors.toString())
                    recyclerViewmain.adapter = adapter
                }
            } catch (e: Exception) {
                // Handle exceptions here
                e.printStackTrace()
            } finally {
                DonorlistUrlConnection.disconnect()
            }
        }
        }
    }

    private fun openDonationTree() {
        val fragment = DonationTree()
        fragment.setImage(image1)// 이미지 설정

        // DonationTree 프래그먼트를 표시
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        fragment.show(transaction, "donationTreeDialog")
    }
}

