package com.example.chart

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SingupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_up)

        signup_btn.setOnClickListener {
            val email = signup_email.editText?.text?.toString() ?: ""
            val name = signup_name.editText?.text?.toString() ?: ""

            if (email.isNotEmpty() && MainActivity.checkEmail(email)) {
                if (name.isNotEmpty() && name.length > 3) {
                    val intent = Intent(this, GeneratePass2::class.java)
                    intent.putExtra("Login", false)
                    intent.putExtra("Email", email)
                    intent.putExtra("Name", name)
                    startActivity(intent)
                } else {
                    signup_name.error = "Name is not valid"
                }

            } else {
                signup_email.error = "Please input a valid email"
            }

        }

        signup_signup.setOnClickListener {
            nextActivity<MainActivity>()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
