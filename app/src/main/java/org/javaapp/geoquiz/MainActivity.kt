package org.javaapp.geoquiz

import android.app.ProgressDialog.show
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
import org.javaapp.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var scoreTextView : TextView
    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    private var currentIndex = 0

    private val questionNumber  = questionBank.size // 총 문제 수
    private var correctNumber  = 0 // 맞춘 문제 수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.score_text_view)
        questionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)

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
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        prevButton.setOnClickListener {
            currentIndex = if (currentIndex == 0) {
                questionBank.size - 1
            } else {
                (currentIndex - 1) % questionBank.size
            }
            updateQuestion()
        }

        updateQuestion()
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        // 이미 맞춘 문제일 경우 버튼 비활성화(숨기기)
        if (questionBank[currentIndex].isCorrect) {
            trueButton.isVisible = false
            falseButton.isVisible = false
        } else {
            trueButton.isVisible = true
            falseButton.isVisible = true
        }

        // 점수 표시
        scoreTextView.setText(String.format("점수 : %.2f  (%d / %d)", correctNumber.toDouble()/questionNumber.toDouble() * 100, correctNumber, questionNumber))
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            questionBank[currentIndex].isCorrect = true
            correctNumber++
            R.string.correct_toast
        } else {
            questionBank[currentIndex].isCorrect = false
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show() // Activity는 Context의 하위 클래스이므로 this를 사용할 수 있다.
    }
}