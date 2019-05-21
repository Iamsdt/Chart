package com.example.chart.db

import androidx.room.*

@Dao
interface TableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(myTable: MyTable): Long

    @Delete
    suspend fun delete(myTable: MyTable): Int

    @Query("Select * From MyTable where email =:email")
    suspend fun search(email: String): MyTable?
}