package com.example.chart.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyTable::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract val dao: TableDao
}

object DbHelper {

    private var myDatabase: MyDatabase? = null

    @Synchronized
    fun getDatabase(context: Context): TableDao {
        if (myDatabase == null) {
            myDatabase = Room.databaseBuilder(
                context,
                MyDatabase::class.java, "MyDatabse"
            ).build()
        }

        return myDatabase!!.dao
    }

}