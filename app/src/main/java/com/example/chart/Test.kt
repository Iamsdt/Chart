package com.example.chart

fun main() {
    var pass = "B"

    val index = pass.length
    val str = pass.removeRange(index - 1, index)
    print(str)
}