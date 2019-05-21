package com.example.chart.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyTable(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var pass: String = "",
    var email: String = ""
)