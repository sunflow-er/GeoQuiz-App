package org.javaapp.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId : Int, val answer : Boolean, var isMarked : Boolean = false, var isCorrect : Boolean = false, var isCheated : Boolean = false)

