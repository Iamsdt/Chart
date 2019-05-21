package com.example.chart


class CreateMyLayout {
    fun getArray(): MutableList<Char> {
        val array = mutableListOf<Char>()
        for ((p, i) in (97..122).withIndex()) {
            val c = i.toChar()
            array.add(c)
        }
        return array
    }

}