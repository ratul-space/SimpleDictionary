package com.example.simpledictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WordDetailActivity : AppCompatActivity() {
    companion object{
        const val WORD_ID = "WORD_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)
    }
}