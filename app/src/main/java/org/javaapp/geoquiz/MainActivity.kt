package org.javaapp.geoquiz

import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import org.javaapp.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity" // 로그 확인용 태그
private const val KEY_INDEX = "index" // Bundle 객체에 저장될 데이터의 키로 사용
private const val REQUEST_CODE_CHEAT = 0 // 요청코드로 사용

class MainActivity : AppCompatActivity() {
    private lateinit var scoreTextView : TextView
    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button

    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        // Bundle 객체에 저장된 값을 확인
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        // ViewModel에 정보 전달
        quizViewModel.currentIndex = currentIndex
        
//        // 현재 액티비티(MainActivity)를 QuizViewModel 인스턴스와 연결
//        val provider : ViewModelProvider = ViewModelProvider(this)
//        /*
//        this : ViewModel이 연결될 Activity 또는 Fragment를 가리킨다.
//        provider : ViewModel 객체를 생성하거나 이미 생성된 ViewModel을 반환하는 역할
//         */
//        val quizViewModel = provider.get(QuizViewModel::class.java)
//        /*
//        get() : 지정된 클래스의 ViewModel 인스턴스를 반환한다. 이미 있으면 그것을 반환, 없으면 새로 생성하여 반환
//        QuizViewModel::class.java : 가져올 ViewModel의 클래스
//         */
//        Log.d(TAG, "Got a QuizViewModel : $quizViewModel")

        scoreTextView = findViewById(R.id.score_text_view)
        questionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)

        questionTextView.setOnClickListener {
            nextButton.performClick()
        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            updateQuestion() // UI 업데이트
        }

        falseButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                checkAnswer(false)
                updateQuestion() // UI 업데이트
            }
        })

        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            // CheatActivity를 시작시킨다.
            val answer = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answer)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.currentQuestionIsCheated = data?.getBooleanExtra(EXTRA_IS_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex) // currentIndex의 값을 Bundle 객체에 저장
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        // 이미 맞춘 문제일 경우 버튼 비활성화(숨기기)
        if (quizViewModel.currentQuestionCorrectness) {
            trueButton.isVisible = false
            falseButton.isVisible = false
        } else {
            trueButton.isVisible = true
            falseButton.isVisible = true
        }

        // 점수 표시
        scoreTextView.setText(String.format("점수 : %.2f  (%d / %d)", quizViewModel.correctNumber.toDouble()/quizViewModel.questionNumber.toDouble() * 100, quizViewModel.correctNumber, quizViewModel.questionNumber))
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.currentQuestionIsCheated -> {
                R.string.judgement_toast
            }
            userAnswer == correctAnswer -> {
                quizViewModel.currentQuestionCorrectness = true
                quizViewModel.correctNumber++
                R.string.correct_toast
            }
            else -> {
                quizViewModel.currentQuestionCorrectness = false
                R.string.incorrect_toast
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show() // Activity는 Context의 하위 클래스이므로 this를 사용할 수 있다.
    }
}