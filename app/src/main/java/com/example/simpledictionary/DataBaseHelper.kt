package com.example.simpledictionary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.preference.PreferenceManager
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_ID
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_MEANING
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_TYPE
import com.example.simpledictionary.DictionaryEntryContract.Companion.COLUMN_WORD
import com.example.simpledictionary.DictionaryEntryContract.Companion.TABLE_NAME
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Error

class DataBaseHelper(private var mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "simple_dict.db"
        private const val DATABASE_VERSION = 1  // Increment the version to trigger an upgrade
        public val DB_CREATED = "DB_CREATED"
    }
    private var mCreateDb = false
    private var mUpgradeDb = false
    override fun onCreate(db: SQLiteDatabase?) {
        mCreateDb = true
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion){
            mUpgradeDb = true
        }
    }

    override fun onOpen(db: SQLiteDatabase?) {
        if (mCreateDb){
            mCreateDb = false
           copyDatabaseFromAssets(db)
        }else if (mUpgradeDb){
            mUpgradeDb = false
            copyDatabaseFromAssets(db)
        }
    }

    private fun copyDatabaseFromAssets(db: SQLiteDatabase?){
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null

        try {
            inputStream = mContext.assets.open(DATABASE_NAME)
            fileOutputStream = FileOutputStream(db?.path)

        var buffer = ByteArray(1024)
        var length = inputStream!!.read(buffer)
         while (length > 0){
             fileOutputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
         }
            fileOutputStream.flush()
//            Validation
           val copiedDb = mContext.openOrCreateDatabase(DATABASE_NAME, 0, null)
           val isDbCreated = copiedDb!= null

            copiedDb.execSQL("PRAGMA user_version = $DATABASE_VERSION")
            copiedDb.close()

//            SAVING DB CREATE STATUS
            val sheredPref = PreferenceManager.getDefaultSharedPreferences(mContext)
            val sheredPrefEditor = sheredPref.edit()
            sheredPrefEditor.putBoolean(DB_CREATED, isDbCreated)
            sheredPrefEditor.apply()

        }catch (e: IOException){
            e.printStackTrace()
            throw Error("copyDatabaseFromAssets: Error coping database")
        }finally {
            try {
                fileOutputStream?.close()
                inputStream?.close()
            }catch (e: IOException){
                e.printStackTrace()
                throw Error(" copyDatabaseFromAssets: Error in closing steam")
            }
        }
    }
    fun getWords(wordsPrefix: String = ""): Cursor {
//        val columns = arrayOf(
//            COLUMN_ID,
//            COLUMN_WORD,
//            COLUMN_TYPE,
//            COLUMN_MEANING
//        )

        if (wordsPrefix.isBlank()) {
            return readableDatabase.rawQuery("select * from $TABLE_NAME", null)
//                TABLE_NAME,
//                columns,
//                null,
//                null,
//                null,
//                null,
//                "$COLUMN_WORD ASC"
//            )
        } else {
            return readableDatabase.rawQuery("select * from $TABLE_NAME where word like '$wordsPrefix%'", null)
//                TABLE_NAME,
//                columns,
//                "$COLUMN_WORD like ?",
//                arrayOf("$wordsPrefix%"),
//                null,
//                null,
//                "$COLUMN_WORD ASC"

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