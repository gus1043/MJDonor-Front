package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.*
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var Fragment1: Fragment1
    private lateinit var Fragment2: Fragment2
    private lateinit var Fragment3: Fragment3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Fragment1 = Fragment1()
        Fragment2 = Fragment2()
        Fragment3 = Fragment3()

        supportFragmentManager.beginTransaction().replace(R.id.container, Fragment1).commit()

        binding.bottomMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_home -> replaceFragment(Fragment1)
                R.id.tab_bookmark -> replaceFragment(Fragment2)
                R.id.tab_mypage -> replaceFragment(Fragment3)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
