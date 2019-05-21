package com.example.chart

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cc.duduhuo.util.digest.Digest
import com.example.chart.db.DbHelper
import com.example.chart.db.MyTable
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_generate_pass2.*
import kotlinx.android.synthetic.main.content_main2.*
import kotlinx.android.synthetic.main.layout_new.*
import kotlinx.coroutines.*
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import kotlin.random.Random

class GeneratePass2 : AppCompatActivity() {

    var pass = ""

    var color = ""

    private val job = SupervisorJob()
    val uiScope = CoroutineScope(Dispatchers.Default + job)

    private var state = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_pass2)
        setSupportActionBar(toolbar)

        val status = intent.getBooleanExtra("Login", true)
        val email = intent.getStringExtra("Email") ?: ""
        val name = intent.getStringExtra("Name") ?: ""

        pyChartGenerate(Generator.generateColorList())

        generateChart(getCapArray(), Generator.generatorColorList())

        cap_button?.setOnClickListener {
            generateChart(getCapArray(), Generator.generatorColorList())
        }

        small_button?.setOnClickListener {
            generateChart(getSmallArray(), Generator.generatorColorList())
        }

        num_button?.setOnClickListener {
            generateChart(generateNumberList(), Generator.generatorColorList())
        }

        symbol_button?.setOnClickListener {
            generateChart(generateSymbolsList(), Generator.generatorColorList())
        }

        imageView2.setOnClickListener {
            if (pass.isNotEmpty()) {
                pass = pass.removeRange(pass.length - 1, pass.length)
                updatePass()
            }
        }

        fab.setOnClickListener {
            if (status) {
                sigin(pass, email)
            } else {
                checkValid(pass, name, email)
            }
        }

        imageView.setOnClickListener {
            state = !state
            updatePass()
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updatePass() {
        if (state) {
            pass_text.text = pass
        } else {
            var txt = ""
            for (i in 1..pass.length) {
                txt += "*"
            }
            pass_text.text = txt
        }
    }

    private fun sigin(mpass: String, email: String) {
        uiScope.launch {
            val dao = DbHelper.getDatabase(this@GeneratePass2)
            val data = withContext(Dispatchers.IO) {
                dao.search(email)
            }
            if (data == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GeneratePass2, "No account found, Please create one!", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }

            } else {
                val text = color + mpass
                val pas = Digest.sha1Hex(text, true)
                if (pas == data.pass) {
                    val intent = Intent(this@GeneratePass2, SuccessActivity::class.java)
                    intent.putExtra("Email", email)
                    startActivity(intent)
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        pass = ""
                        color = ""
                        Toast.makeText(this@GeneratePass2, "Password don't match", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun generateNumberList(): List<String> {
        val array = mutableListOf<String>()
        for ((p, i) in (0..9).withIndex()) {
            array.add("$i")
        }

        val int = Random.nextInt(25)
        return array.shuffled(Random(int))
    }

    private fun generateSymbolsList(): List<String> {
        val array = mutableListOf(
            "@",
            "!",
            "#",
            "$",
            "%",
            "*",
            "&",
            "?",
            "<",
            ">"
        ).shuffled()

        val int = Random.nextInt(25)
        return array.shuffled(Random(int))
    }

//    private fun pyChartGenerate(list: List<PieEntry>) {
//        val data = PieDataSet(list, "ColorList")
//        data.label(false)
//        data.isValueLabelBackgroundAuto = false
//        data.isValueLabelBackgroundEnabled = false
//        data.setHasCenterCircle(true)
//        data.centerCircleScale = 0.80f
//        pieChart.pieChartData = data
//        pieChart.isChartRotationEnabled = true
//        pieChart.onValueTouchListener = object : PieChartOnValueSelectListener {
//            override fun onValueSelected(arcIndex: Int, value: SliceValue?) {
//                value?.let {
//                    val v = String(value.labelAsChars)
//                    color = v
//                    //pieChart.setChartRotation()
//                }
//            }
//
//            override fun onValueDeselected() {
//                //nothing to do
//            }
//        }
//    }

    private fun pyChartGenerate(list: List<PieEntry>) {
        val set = PieDataSet(list, "ColorList")
        set.setDrawValues(false)
        val data = PieData(set)
        data.setDrawValues(false)
        set.colors = Generator.colorList()
        pieChart.data = data
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 0.8f
        pieChart.setDrawEntryLabels(false)

        //set description
        val des = Description()
        des.text = ""
        pieChart.description = des

        pieChart.setDrawMarkers(false)
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                //nothing to do
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val string = e?.data.toString()
                color = string
            }

        })

    }

    private fun getSmallArray(): List<String> {
        val array = mutableListOf<String>()
        for ((p, i) in (97..122).withIndex()) {
            val c = i.toChar()
            array.add("$c")
        }

        val int = Random.nextInt(25)
        return array.shuffled(Random(int))
    }

    private fun getCapArray(): List<String> {
        val array = mutableListOf<String>()
        for ((p, i) in (65..90).withIndex()) {
            val c = i.toChar()
            array.add("$c")
        }
        val int = Random.nextInt(25)
        return array.shuffled(Random(int))
    }

    private fun generateChart(list: List<String>, color: List<Int>) {
        for (i in 0 until list.size) {
            val tv = getTev(i)
            tv.visibility = View.VISIBLE
            val text = list[i]
            tv.text = text
            tv.setTextColor(color[i])
            tv.setOnClickListener {
                pass += text
                updatePass()
            }
        }

        if (list.size < 20) {
            var i = 10
            while (i < 26) {
                val tv = getTev(i)
                tv.text = ""
                tv.visibility = View.GONE
                i++
            }
        }
    }

    private fun getTev(position: Int) =
        when (position) {
            0 -> a1
            1 -> a2
            2 -> a3
            3 -> a4
            4 -> a5
            5 -> a6
            6 -> a7
            7 -> a8
            8 -> a9
            9 -> a10
            10 -> a11
            11 -> a12
            12 -> a13
            13 -> a14
            14 -> a15
            15 -> a16
            16 -> a17
            17 -> a18
            18 -> a19
            19 -> a20
            20 -> a21
            21 -> a22
            22 -> a23
            23 -> a24
            24 -> a25
            25 -> a26
            else -> a1
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

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel(CancellationException("Activity destroyed"))
    }


    private fun createAccount(pass: String, name: String, email: String) {
        uiScope.launch {
            val table = MyTable(0, name, pass, email)
            val dao = DbHelper.getDatabase(this@GeneratePass2)
            val status = withContext(Dispatchers.IO) {
                dao.add(table)
            }
            if (status > 0) {
                val intent = Intent(this@GeneratePass2, SuccessActivity::class.java)
                intent.putExtra("Email", email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@GeneratePass2, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
