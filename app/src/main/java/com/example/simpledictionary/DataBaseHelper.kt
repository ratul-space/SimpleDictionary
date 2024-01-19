package com.example.simpledictionary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_ID
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_MEANING
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_TYPE
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_WORD
import com.example.simpledictionary.DictionaryEntryContract.Companion.TABLE_NAME

class DataBaseHelper(private var mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "simple_dict.db"
        private const val DATABASE_VERSION = 2  // Increment the version to trigger an upgrade
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_WORD TEXT," +
                    "$COLUMN_TYPE TEXT," +
                    "$COLUMN_MEANING TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addSomeDummyWords() {
        val dummyWords = arrayOf("a", "apple", "b", "ball", "c", "cat", "d", "dog", "e", "orange")
        val contentValues = ContentValues()
        var id = 1
        for (word in dummyWords) {
            contentValues.put(COLUMN_ID, id)
            contentValues.put(COLUMN_WORD, word)
            contentValues.put(COLUMN_TYPE, "noun")
            contentValues.put(COLUMN_MEANING, "This is an english alphabet")
            this.writableDatabase.insert(TABLE_NAME, null, contentValues)
            id++
        }
    }

    fun getWords(wordsPrefix: String = ""): Cursor {
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_WORD,
            COLUMN_TYPE,
            COLUMN_MEANING
        )

        if (wordsPrefix.isBlank()) {
            return readableDatabase.query(
                TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                "$COLUMN_WORD ASC"
            )
        } else {
            return readableDatabase.query(
                TABLE_NAME,
                columns,
                "$COLUMN_WORD like ?",
                arrayOf("$wordsPrefix%"),
                null,
                null,
                "$COLUMN_WORD ASC"
            )
        }
    }

    fun getWord(id: String): Cursor {
        return readableDatabase.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )
    }
}