package org.javaapp.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel" // const 키워드는 컴파일 타임 상수를 정의하는데 사용 (변경 불가능 & 값이 컴파일 시점에 알려져야 함)

class QuizViewModel : ViewModel() {
    
    init { // 초기화 블록 : 클래스의 인스턴스가 생성될 때 실행되는 코드, 생성자보다 먼저 실행된다.
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() { // ViewModel 인스턴스가 소멸되기 전에 호출, 클린업할 것이 있으면 이 함수에서 하면 된다.
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    var currentIndex = 0

    val questionNumber = questionBank.size
    var correctNumber = 0

    val currentQuestionAnswer : Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText : Int
        get() = questionBank[currentIndex].textResId

    var currentQuestionCorrectness : Boolean
        get() = questionBank[currentIndex].isCorrect
        set(value) { questionBank[currentIndex].isCorrect = value }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            currentIndex - 1
        }
    }
}