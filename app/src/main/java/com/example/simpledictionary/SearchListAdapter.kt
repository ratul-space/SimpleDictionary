package com.example.simpledictionary

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SearchListAdapter(context: Context, cursor: Cursor):CursorAdapter(context, cursor, 0) {
    private class ViewHolder{
        var textWord: TextView? = null
        var textType: TextView? = null
        var textMeaning: TextView? = null

        var wordColumnIndex: Int = 0
        var typeColumnIndex: Int = 0
        var meaningColumnIndex: Int = 0
    }
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
       val layoutInflater = LayoutInflater.from(context)
        val newView = layoutInflater.inflate(R.layout.search_list_item, parent, false)

        val viewHolder = ViewHolder()
        viewHolder.textWord = newView.findViewById<TextView >(R.id.txtWord)
        viewHolder.textType = newView.findViewById<TextView >(R.id.txtType)
        viewHolder.textMeaning = newView.findViewById<TextView >(R.id.txtMeaning)

        viewHolder.wordColumnIndex = cursor!!.getColumnIndexOrThrow(DictionaryEntryContract.COLUMN_WORD)
        viewHolder.wordColumnIndex = cursor!!.getColumnIndexOrThrow(DictionaryEntryContract.COLUMN_TYPE)
        viewHolder.meaningColumnIndex = cursor!!.getColumnIndexOrThrow(DictionaryEntryContract.COLUMN_MEANING)

        newView.tag = viewHolder

        return newView
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {

        val viewHolder = view!!.tag as ViewHolder

        viewHolder.textWord?.text = cursor?.getString(viewHolder.wordColumnIndex)
        viewHolder.textType?.text = cursor?.getString(viewHolder.wordColumnIndex)
        viewHolder.textMeaning?.text = cursor?.getString( viewHolder.meaningColumnIndex)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view =  super.getView(position, convertView, parent)
        if (position % 2 == 0){
            view.setBackgroundColor(Color.LTGRAY)
        }else{
            view.setBackgroundColor(Color.WHITE)
        }
        return view
    }
}