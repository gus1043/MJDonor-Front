package com.example.myapplication

import DonorAdapter
import DonorItem
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDonationselectionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DonSelectActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var donLoc: String
    private lateinit var description: String
    private lateinit var image: String
    private var p_id : Int=0
    private var enddate : Int=0
    private var goal : Int=0
    private var current: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDonationselectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트로 전달된 값 받기
        title = intent.getStringExtra("title") ?: "Default Title"
        donLoc = intent.getStringExtra("donLoc") ?: "Default Location"
        image = intent.getStringExtra("image")?: "Default Location"
        p_id = intent.getIntExtra("p_id",1)
        goal = intent.getIntExtra("goal",1)
        enddate = intent.getIntExtra("enddate",1)
        current = intent.getIntExtra("current",1)

        description = intent.getStringExtra("description") ?: "Default Description"

        val endDateDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(enddate.toString())
        val formattedEndDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(endDateDate)
        val formattedGoal = NumberFormat.getNumberInstance(Locale.getDefault()).format(goal)
        val formattedCurrent = NumberFormat.getNumberInstance(Locale.getDefault()).format(current)

        binding.selecteddonation.setOnClickListener {
            openDonationTree()
        }

        binding.selectdonatebtn.setOnClickListener {
            val intent: Intent = Intent(this, DonInputActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
        }

        // 받은 값을 뷰에 설정
        binding.selectedtitle.text = title
        binding.company.text = donLoc
        Glide.with(this)
            .load(image)
            .into(binding.selecteddonation)
        binding.donationContent.text = description
        binding.goal.text = "$formattedGoal 원"
        binding.enddate.text = "$formattedEndDate 까지"
        binding.current.text="$formattedCurrent 원"

        val recyclerViewmain = binding.recyclerViewmain
        val itemList = listOf(
            DonorItem( "https://placebear.com/g/200/200",1),
            DonorItem("https://placebear.com/g/200/200",1),
            DonorItem("https://cdn.shopify.com/s/files/1/1830/5085/products/VE0007_BCAA_Capsule_90ct_2048x2048.png?v=1494855182",1),
            DonorItem( "https://cdn.shopify.com/s/files/1/1830/5085/products/VE0007_BCAA_Capsule_90ct_2048x2048.png?v=1494855182",1),
            DonorItem(  "https://via.placeholder.com/300.png/09f/fff",1),
            DonorItem( "https://source.unsplash.com/user/c_v_r/1900×800",1),
            DonorItem( "https://source.unsplash.com/user/c_v_r/1900×800",1),
            DonorItem( "https://source.unsplash.com/user/c_v_r/1900×800",2),
            DonorItem(  "https://via.placeholder.com/300.png/09f/fff",2),
            DonorItem("https://cdn.shopify.com/s/files/1/1830/5085/products/VE0007_BCAA_Capsule_90ct_2048x2048.png?v=1494855182",2),
            DonorItem( "https://placebear.com/g/200/200",2),
            DonorItem( "https://via.placeholder.com/300.png/09f/fff",3),
            DonorItem( "https://cdn.shopify.com/s/files/1/1830/5085/products/VE0007_BCAA_Capsule_90ct_2048x2048.png?v=1494855182",3),
        )

        val filteredItemList = itemList.filter { donorItem ->
            donorItem.p_id == p_id
        }

        val adapter = DonorAdapter(this, filteredItemList)
        recyclerViewmain.adapter = adapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewmain.layoutManager = layoutManager

    }

    private fun openDonationTree() {
        val fragment = DonationTree()
        fragment.show(supportFragmentManager, "donationTreeDialog")
    }
}

