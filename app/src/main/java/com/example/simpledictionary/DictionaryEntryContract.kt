package com.example.simpledictionary

import android.provider.BaseColumns

class DictionaryEntryContract : BaseColumns {
    private constructor()

    companion object {
        const val TABLE_NAME = "english_words"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_WORD = "word"
        const val COLUMN_TYPE = "type"
        const val COLUMN_MEANING = "meaning"
    }
}
