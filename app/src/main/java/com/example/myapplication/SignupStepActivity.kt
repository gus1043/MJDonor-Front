package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.example.myapplication.databinding.ActivitySignupstepBinding

class SignupStepActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupstepBinding
    private var position = 0

    companion object {
        private const val STEP_1 = 0
        private const val STEP_2 = 1
        private const val FINAL_STEP = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupstepBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.stepView.done(false)

        binding.button.setOnClickListener {
            when (position) {
                STEP_1 -> transitionToStep(STEP_2, "Next")
                STEP_2 -> transitionToStep(FINAL_STEP, "Submit")
                else -> {
                    navigateToLoginActivity()
                }
            }
        }
    }

    override fun onBackPressed() {
        when (position) {
            STEP_1 -> super.onBackPressed()
            STEP_2 -> transitionToStep(STEP_1, "Next")
            else -> transitionToStep(STEP_2, "Next")
        }
    }

    private fun transitionToStep(nextPosition: Int, buttonText: String) {
        when (position) {
            STEP_1 -> binding.STEP1.visibility = View.GONE
            STEP_2 -> binding.STEP2.visibility = View.GONE
            FINAL_STEP -> binding.FINALSTEP.visibility = View.GONE
        }
        position = nextPosition
        binding.stepView.done(false)
        binding.stepView.go(position, true)
        binding.button.text = buttonText
        when (position) {
            STEP_1 -> binding.STEP1.visibility = View.VISIBLE
            STEP_2 -> binding.STEP2.visibility = View.VISIBLE
            FINAL_STEP -> binding.FINALSTEP.visibility = View.VISIBLE
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
