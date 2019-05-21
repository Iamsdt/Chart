package com.example.chart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_login.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        //nextActivity<GeneratePass2>()

        login_pass.setOnClickListener {
            val email = login_email.editText?.text?.toString() ?: ""
            if (email.isNotEmpty() && checkEmail(email)) {
                val intent = Intent(this, GeneratePass2::class.java)
                intent.putExtra("Login", true)
                intent.putExtra("Email", email)
                startActivity(intent)
            } else {
                login_email.error = "Please add valid email"
            }
        }

        forget_pass.setOnClickListener {
            nextActivity<RecoverActivity>()
        }

        sign_in_signBtn.setOnClickListener {
            nextActivity<SingupActivity>()
        }

    }

    companion object {
        fun checkEmail(string: String): Boolean {
            val pattern = ".*[@.].*"
            return string.contains(Regex(pattern))
        }
    }
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.nextActivity(
    extraKey: String = "", extra: Any = "", finish: Boolean = false
) {

    val intent = Intent(this, T::class.java)

    if (extraKey.isNotEmpty()) {
        when (extra) {
            is String -> intent.putExtra(extraKey, extra)
            is Boolean -> intent.putExtra(extraKey, extra)
            is Float -> intent.putExtra(extraKey, extra)
            is Long -> intent.putExtra(extraKey, extra)
            is Int -> intent.putExtra(extraKey, extra)
            is Bundle -> intent.putExtra(extraKey, extra)
            else -> intent.putExtra(extraKey, "$extra")
        }
    }

    startActivity(intent)

    if (finish) finish()
}
