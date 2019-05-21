package com.example.chart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.chart.db.DbHelper
import kotlinx.android.synthetic.main.activity_success.*
import kotlinx.android.synthetic.main.content_success.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SuccessActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        setSupportActionBar(toolbar)

        val emailStr = intent.getStringExtra("Email") ?: ""

        GlobalScope.launch {
            val dao = DbHelper.getDatabase(this@SuccessActivity)

            val table = withContext(Dispatchers.IO) {
                dao.search(emailStr)
            }

            if (table != null) {
                name.text = "Name: ${table.name}"
                email.text = "Email: ${table.email}"
                pass.text = "Password: ${table.pass}"
            } else {
                email.text = "Email: $emailStr"
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        } else if (item.itemId == R.id.action_settings) {
            nextActivity<MainActivity>()
        }

        return super.onOptionsItemSelected(item)
    }

}
