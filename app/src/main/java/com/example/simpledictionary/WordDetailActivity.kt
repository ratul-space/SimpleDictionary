package com.example.simpledictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WordDetailActivity : AppCompatActivity() {
    companion object{
        const val WORD_ID = "WORD_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)

        val wordId = intent.getStringExtra(WORD_ID) ?: ""
        if (wordId.isBlank()){
            finish()
        }
        val dbHelper = DataBaseHelper(applicationContext)
        val cursor = dbHelper.getWord(wordId)
        if (cursor.moveToFirst()){
            val textWord = findViewById<TextView>(R.id.txtWord)
            val textType = findViewById<TextView>(R.id.txtType)
            val textMeaning = findViewById<TextView>(R.id.txtMeaning)

            textWord?.text = cursor.getString(0)
            textType?.text = cursor.getString(1)
            textMeaning?.text = cursor.getString(2)
        }
    }
}