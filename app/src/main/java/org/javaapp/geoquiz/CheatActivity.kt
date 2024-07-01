package org.javaapp.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

const val EXTRA_IS_SHOWN = "com.javaapp.geoquiz.answer_shown"
private const val EXTRA_ANSWER = "org.javaapp.geoquiz.answer_is_true" // 엑스트라 키로 사용할 상수

class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton : Button

    private val cheatViewModel : CheatViewModel by lazy {
        ViewModelProvider(this).get(CheatViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        cheatViewModel.answer = intent.getBooleanExtra(EXTRA_ANSWER, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            cheatViewModel.isCheated = true
            showUI(cheatViewModel.isCheated)
        }

        showUI(cheatViewModel.isCheated)
    }

    private fun showUI(isCheated : Boolean) {
        if (isCheated) {
            val answerText = if (cheatViewModel.answer) {
                R.string.true_button
            } else {
                R.string.false_button
            }

            answerTextView.setText(answerText)

            setAnswerShownResult(true)
        } else {
            answerTextView.setText("")
        }
    }


    private fun setAnswerShownResult(isAnswerShown : Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_IS_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    // CheatActivity가 필요로 하는 엑스트라 데이터를 갖는 인텐트를 생성한다.
    companion object {
        fun newIntent(packageContext: Context, answer: Boolean) : Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER, answer)
            }
        }
    }
}