package com.example.simpledictionary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.simpledictionary.DictionaryEntryContract.Companion.TABLE_NAME

class DataBaseHelper(private var mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "simple_dict.db"
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_NAME(${DictionaryEntryContract.COLUMN_ID} INT, ${DictionaryEntryContract.COLUMN_WORD} TEXT, ${DictionaryEntryContract.COLUMN_TYPE} TEXT, ${DictionaryEntryContract.COLUMN_MEANING} TEXT)")
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
            contentValues.put(DictionaryEntryContract.COLUMN_ID, id)
            contentValues.put(DictionaryEntryContract.COLUMN_WORD, word)
            contentValues.put(DictionaryEntryContract.COLUMN_TYPE, "noun")
            contentValues.put(DictionaryEntryContract.COLUMN_MEANING, "This is an english alphabet")
            this.writableDatabase.insert(TABLE_NAME, null, contentValues)
            id++
        }
    }

    fun getWords(wordsPrefix: String = ""): Cursor {
        if (wordsPrefix.isBlank()) {
            return readableDatabase.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${DictionaryEntryContract.COLUMN_WORD} ASC"
            )

//            return readableDatabase.rawQuery("select * from ${DictionaryEntryContract.TABLE_NAME}", null)
        } else {
            return readableDatabase.query(
                TABLE_NAME,
                null,
                "${DictionaryEntryContract.COLUMN_WORD} like ?",
                arrayOf("$wordsPrefix%"),
                null,
                null,
                "${DictionaryEntryContract.COLUMN_WORD} ASC"
            )

//            return readableDatabase.rawQuery("select * from ${DictionaryEntryContract.TABLE_NAME} where word like '$wordsPrefix%'", null)
        }

//        while (cursor.moveToNext()){
//            val id = cursor.getInt(0)
//            val word = cursor.getString(1)
//            val type = cursor.getString(2)
//            val meaning = cursor.getString(3)
//
//            Log.d("DictonaryActivity", "$id, $word, $type, $meaning")
//        }
    }

    fun getWord(id: String): Cursor {
        return readableDatabase.query(
            TABLE_NAME,
            null,
            "${DictionaryEntryContract.COLUMN_ID} = ?",
            arrayOf("$id"),
            null,
            null,
            null
        )
//        return readableDatabase.rawQuery("select * from ${DictionaryEntryContract.TABLE_NAME} where ${DictionaryEntryContract.COLUMN_ID}=$id", null)
    }
}