package com.example.chart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cc.duduhuo.util.digest.Digest
import com.example.chart.db.DbHelper
import com.example.chart.db.MyTable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

class GeneratePass : AppCompatActivity() {

    var pass = ""

    var color = ""

    private val job = SupervisorJob()
    val uiScope = CoroutineScope(Dispatchers.Default + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = intent.getBooleanExtra("Login", true)
        val email = intent.getStringExtra("Email") ?: ""
        val name = intent.getStringExtra("Name") ?: ""

        //pyChartGenerate(Generator.generateColorList())

        pyChartGenerate2(Generator.generateCapWordList())

        cap_button?.setOnClickListener {
            pyChartGenerate2(Generator.generateCapWordList())
        }

        small_button?.setOnClickListener {
            pyChartGenerate2(Generator.generateSmallWordList())
        }

        num_button?.setOnClickListener {
            pyChartGenerate2(Generator.generateNumberList())
        }

        symbol_button?.setOnClickListener {
            pyChartGenerate2(Generator.generateSymbolsList())
        }

        imageView2.setOnClickListener {
            if (pass.isNotEmpty()) {
                pass = pass.removeRange(pass.length - 1, pass.length)
            }
        }

        fab.setOnClickListener {
            if (status) {
                sigin(pass, email)
            } else {
                checkValid(pass, name, email)
            }
        }

    }

    private fun sigin(pass: String, email: String) {
        uiScope.launch {
            val dao = DbHelper.getDatabase(this@GeneratePass)
            val data = withContext(Dispatchers.IO) {
                dao.search(email)
            }
            if (data == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GeneratePass, "No account found, Please create one!", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } else {
                val text = color + pass
                val pas = Digest.sha1Hex(text, true)
                if (pas == data.pass) {
                    val intent = Intent(this@GeneratePass, SuccessActivity::class.java)
                    intent.putExtra("Email", email)
                    startActivity(intent)
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GeneratePass, "Password don't match", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun pyChartGenerate(list: List<SliceValue>) {
        val data = PieChartData(list)
        data.setHasLabels(false)
        data.isValueLabelBackgroundAuto = false
        data.isValueLabelBackgroundEnabled = false
        data.setHasCenterCircle(true)
        data.centerCircleScale = 0.80f
        pieChart.pieChartData = data
        pieChart.isChartRotationEnabled = false
        pieChart.onValueTouchListener = object : PieChartOnValueSelectListener {
            override fun onValueSelected(arcIndex: Int, value: SliceValue?) {
                value?.let {
                    val v = String(value.labelAsChars)
                    color = v
                }
            }

            override fun onValueDeselected() {
                //nothing to do
            }
        }
    }

    private fun pyChartGenerate2(list: List<SliceValue>) {
        val data = PieChartData(list)
        data.setHasLabels(true)
        data.isValueLabelBackgroundAuto = false
        data.isValueLabelBackgroundEnabled = false
        data.setHasCenterCircle(true)
        inner.pieChartData = data
        inner.onValueTouchListener = object : PieChartOnValueSelectListener {
            override fun onValueSelected(arcIndex: Int, value: SliceValue?) {
                value?.let {
                    val v = String(value.labelAsChars)
                    pass += v
                }
            }

            override fun onValueDeselected() {
                //nothing to do
            }
        }
    }

    private fun checkValid(pass: String, name: String, email: String) {

        if (color.isEmpty()) {
            Toast.makeText(this, "Please select color", Toast.LENGTH_SHORT).show()
            return
        }

        //check length is
        if (pass.length >= 8) {
            if (pass.contains(Regex(".*[a-z].*"))) {
                if (pass.contains(Regex(".*[A-Z].*"))) {
                    val pattern = ".*[@#$%&*?<>|].*"
                    if (pass.contains(Regex(pattern))) {
                        if (pass.contains(Regex(".*[1-9].*"))) {
                            val data = color + pass
                            val en = Digest.sha1Hex(data, true)
                            createAccount(en, name, email)
                        } else {
                            Toast.makeText(this, "Password must contains numbers", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Password must contains special symbols", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Password must contains capital characters", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Password must contains Small characters", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount(pass: String, name: String, email: String) {
        uiScope.launch {
            val table = MyTable(0, name, pass, email)
            val dao = DbHelper.getDatabase(this@GeneratePass)
            val status = withContext(Dispatchers.IO) {
                dao.add(table)
            }
            if (status > 0) {
                val intent = Intent(this@GeneratePass, SuccessActivity::class.java)
                intent.putExtra("Email", email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@GeneratePass, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel(CancellationException("Activity destroyed"))
    }
}
