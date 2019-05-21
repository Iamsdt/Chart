package com.example.chart

import android.graphics.Color
import com.github.mikephil.charting.data.PieEntry
import lecho.lib.hellocharts.model.SliceValue

class Generator {
    companion object {
        fun generateSmallWordList(): List<SliceValue> {
            val array = mutableListOf<SliceValue>()
            for ((p, i) in (97..122).withIndex()) {
                val c = i.toChar()
                array.add(SliceValue(1f, generatorColorList()[p]).setLabel("$c"))
            }

            return array.shuffled()
        }

        fun generateCapWordList(): List<SliceValue> {
            val array = mutableListOf<SliceValue>()

            for ((p, i) in (65..90).withIndex()) {
                val c = i.toChar()
                array.add(SliceValue(1f, generatorColorList()[p]).setLabel("$c"))
            }

            return array.shuffled()
        }

        fun generateNumberList(): List<SliceValue> {
            val array = mutableListOf<SliceValue>()
            for ((p, i) in (0..9).withIndex()) {
                array.add(SliceValue(1f, generatorColorList()[p]).setLabel("$i"))
            }

            return array.shuffled()
        }

        fun generateSymbolsList() = mutableListOf(
            SliceValue(1f, generatorColorList()[0]).setLabel("@"),
            SliceValue(1f, generatorColorList()[1]).setLabel("!"),
            SliceValue(1f, generatorColorList()[2]).setLabel("#"),
            SliceValue(1f, generatorColorList()[3]).setLabel("$"),
            SliceValue(1f, generatorColorList()[4]).setLabel("%"),
            SliceValue(1f, generatorColorList()[5]).setLabel("*"),
            SliceValue(1f, generatorColorList()[6]).setLabel("&"),
            SliceValue(1f, generatorColorList()[7]).setLabel("?"),
            SliceValue(1f, generatorColorList()[8]).setLabel("<"),
            SliceValue(1f, generatorColorList()[9]).setLabel("|")
        ).shuffled()


        fun generateColorList() =
            listOf(
                PieEntry(1f, Color.RED),
                PieEntry(1f, Color.YELLOW),
                PieEntry(1f, Color.BLUE),
                PieEntry(1f, Color.GREEN),
                PieEntry(1f, Color.CYAN),
                PieEntry(1f, Color.MAGENTA),
                PieEntry(1f, Color.BLACK),
                PieEntry(1f, Color.GRAY)
            )

        fun colorList() = listOf(
            Color.RED,
            Color.YELLOW,
            Color.BLUE,
            Color.GREEN,
            Color.CYAN,
            Color.MAGENTA,
            Color.BLACK,
            Color.GRAY
        )


        fun generatorColorList() =
            listOf(
                Color.parseColor("#e51c23"),
                Color.parseColor("#ffeb3b"),
                Color.parseColor("#5677fc"),
                Color.parseColor("#259b24"),
                Color.parseColor("#00bcd4"),
                Color.parseColor("#ff9800"),
                Color.parseColor("#03a9f4"),
                Color.parseColor("#cddc39"),
                Color.parseColor("#ff5722"),
                Color.parseColor("#8bc34a"),
                Color.parseColor("#ffc107"),
                Color.parseColor("#9c27b0"),
                Color.parseColor("#795548"),
                Color.parseColor("#3f51b5"),
                Color.parseColor("#0a7e07"),
                Color.parseColor("#0a7e07"),
                Color.parseColor("#f57c00"),
                Color.parseColor("#D81B60"),
                Color.parseColor("#f57c00"),
                Color.parseColor("#afb42b"),
                Color.parseColor("#455ede"),
                Color.parseColor("#0d5302"),
                Color.parseColor("#303f9f"),
                Color.parseColor("#689f38"),
                Color.parseColor("#00574B"),
                Color.parseColor("#0097a7"),
                Color.parseColor("#455ede")
            ).shuffled()
    }
}