package org.javaapp.geoquiz

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import org.javaapp.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 시스템UI로부터의 여백을 가져와 뷰의 패딩으로 설정, 시스템UI와 앱의 콘텐츠가 겹치지 않도록 한다.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.trueButton.setOnClickListener { view: View ->
            Toast.makeText(this@MainActivity, R.string.correct_toast, Toast.LENGTH_SHORT).show() // Activity는 Context의 하위 클래스이므로 this를 사용할 수 있다.

        }

        binding.falseButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()

            }
        })
    }
}