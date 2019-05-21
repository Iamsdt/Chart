package com.example.chart

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.example.chart.db.DbHelper

import kotlinx.android.synthetic.main.activity_recover.*
import kotlinx.android.synthetic.main.content_recover.*
import kotlinx.android.synthetic.main.content_recover.signup_btn
import kotlinx.android.synthetic.main.content_recover.signup_email
import kotlinx.android.synthetic.main.content_recover.signup_name
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.*

class RecoverActivity : AppCompatActivity() {

    private val job = SupervisorJob()
    val uiScope = CoroutineScope(Dispatchers.Default + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)
        setSupportActionBar(toolbar)


        signup_btn.setOnClickListener {
            val email = signup_email.editText?.text?.toString() ?: ""
            val name = signup_name.editText?.text?.toString() ?: ""

            if (email.isNotEmpty() && MainActivity.checkEmail(email)) {
                if (name.isNotEmpty() && name.length > 3) {
                    val dao = DbHelper.getDatabase(this@RecoverActivity)
                    uiScope.launch {
                        val table = withContext(Dispatchers.IO) { dao.search(email) }
                        if (table != null) {
                            val intent = Intent(this@RecoverActivity, GeneratePass2::class.java)
                            intent.putExtra("Login", false)
                            intent.putExtra("Email", email)
                            intent.putExtra("Name", name)
                            startActivity(intent)
                        } else {
                            recoverLay.showSnackbar("No account found with this email")
                        }
                    }
                } else {
                    signup_name.error = "Name is not valid"
                }

            } else {
                signup_email.error = "Please input a valid email"
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    fun View.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(this, snackbarText, timeLength).show()
    }

}
