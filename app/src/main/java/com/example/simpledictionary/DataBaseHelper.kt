package com.example.simpledictionary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper(private var mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "simple_dict.db"
        private val DATABASE_VERSION = 1

        val TABLE_NAME = "english_words"

        val COLUMN_ID = "id"
        val COLUMN_WORD = "word"
        val COLUMN_TYPE = "type"
        val COLUMN_MEANING = "meaning"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_NAME($COLUMN_ID INT, $COLUMN_WORD TEXT, $COLUMN_TYPE TEXT, $COLUMN_MEANING TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")

        onCreate(db)
    }

    fun addSomeDummyWords() {

        val dummyWords = arrayOf("a", "apple", "b", "ball", "c", "cat", "d", "dog", "e", "engle")
        val contentValues = ContentValues()
        var id = 1
        for (word in dummyWords) {
            contentValues.put(COLUMN_ID, 1)
            contentValues.put(COLUMN_WORD, "a")
            contentValues.put(COLUMN_TYPE, "noun")
            contentValues.put(COLUMN_MEANING, "First letter of english alphabet")
            this.writableDatabase.insert(TABLE_NAME, null, contentValues)
        }
    }

    fun getWords(): Cursor {
        val cursor: Cursor = readableDatabase.rawQuery("select * from $TABLE_NAME", null)

//        while (cursor.moveToNext()){
//            val id = cursor.getInt(0)
//            val word = cursor.getString(1)
//            val type = cursor.getString(2)
//            val meaning = cursor.getString(3)
//
//            Log.d("DictonaryActivity", "$id, $word, $type, $meaning")
//        }
        return cursor
    }
}