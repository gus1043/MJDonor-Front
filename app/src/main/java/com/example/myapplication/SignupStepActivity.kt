package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivitySignupstepBinding
import java.util.regex.Pattern

class SignupStepActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupstepBinding
    private var position = 0

    val TAG: String = "회원가입"
    var isExistBlank = false
    var isPWSame = false
    var studentNumCorrect = true
    var passwordCorrect = true
    var emailCorrect = true

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
                    val email = binding.email.text.toString()
                    val password= binding.password.text.toString()
                    val passwordCheck= binding.passwordCheck.text.toString()
                    val name= binding.name.text.toString()
                    val studentNum = binding.studentNum.text.toString()
                    val walletAdress = binding.walletAddress.text.toString()

                    // 유저가 항목을 다 채우지 않았을 경우
                    if(email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()|| name.isEmpty()|| studentNum.isEmpty()|| walletAdress.isEmpty()){
                        isExistBlank = true
                    }
                    else{
                        if(password == passwordCheck){
                            isPWSame = true
                        }
                    }

                    //이메일 유효성 체크
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailCorrect = false
                    }

                    //학번 유효성
                    if(!Pattern.matches("(?:\\d{8})", studentNum))
                    {
                        studentNumCorrect= false
                    }

//                    //비밀번호 유효성
//                    if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", password))
//                    {
//                       passwordCorrect= false
//                    }

                    if(!isExistBlank && isPWSame && emailCorrect && studentNumCorrect){

                        // 회원가입 성공 토스트 메세지 띄우기
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                        // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                        val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putString("id", email)
                        editor.putString("pw", password)
                        editor.apply()

                        // 로그인 화면으로 이동
                        navigateToLoginActivity()

                    }
                    else{
                        // 상태에 따라 다른 다이얼로그 띄워주기
                        if(isExistBlank){   // 작성 안한 항목이 있을 경우
                            dialog("blank")
                        }
                        else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
                            dialog("not same")
                        }
//                        else if(!passwordCorrect){ // 비밀번호 유효성
//                            dialog("passwordCorrect")
//                        }
                        else if(!emailCorrect){ // 입력한 비밀번호가 다를 경우
                            dialog("emailCorrect")
                        }
                        else if(!studentNumCorrect){ // 입력한 비밀번호가 다를 경우
                            dialog("studentNumCorrect")
                        }
                    }
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
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 정확히 작성해주세요")
        }
        // 입력한 비밀번호가 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }
        else if(type.equals("studentNumCorrect")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("학번을 8자를 입력해주세요")
        }
        else if(type.equals("emailCorrect")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("이메일 형식을 맞춰주세요")
        }
//        else if(type.equals("passwordCorrect")){
//            dialog.setTitle("회원가입 실패")
//            dialog.setMessage("비밀번호 형식을 맞춰 주세요")
//        }

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
}
