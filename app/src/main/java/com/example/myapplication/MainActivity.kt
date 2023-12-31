package com.example.myapplication

import Fragment1
import Fragment2
import Fragment3
import Fragment4
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var fragment1: Fragment1
    private lateinit var fragment2: Fragment2
    private lateinit var fragment3: Fragment3
    private lateinit var fragment4: Fragment4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // isFirstTime이랑 LoginActivity의 login랑은 반대임
        // isFirstTime가 login받은 거니까 false일 때 동의한 거
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE) // 앱 자체에 데이터 저장
        val isFirstTime = sharedPreferences.getBoolean("login", false)

        fragment1 = Fragment1()
        fragment2 = Fragment2()
        fragment3 = Fragment3()
        fragment4 = Fragment4()

        val selectedFragment = intent.getStringExtra("selectedFragment")

        if (selectedFragment == "fragment3") {
            replaceFragment(fragment3)
        } else {
            replaceFragment(fragment1)
        }
        if (!isFirstTime) {
            // isFirstTime이 true인 경우 동작 실행
            binding.bottomMenu.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.tab_home -> replaceFragment(fragment1)
                    R.id.tab_list,
                    R.id.tab_add,
                    R.id.tab_mypage -> {
                        // 로그인으로 이동
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fromright_toleft, R.anim.none)
                    }
                }
                true
            }
        }else{

            binding.bottomMenu.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.tab_home -> replaceFragment(fragment1)
                    R.id.tab_list -> replaceFragment(fragment2)
                    R.id.tab_add -> replaceFragment(fragment4)
                    R.id.tab_mypage -> replaceFragment(fragment3)
                }
                true
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
