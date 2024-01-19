package com.example.simpledictionary

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class SearchListAdapter(context: Context, cursor: Cursor):CursorAdapter(context, cursor, 0) {
    private class viewHolder{
        val textWord: TextView? = null
        val textType: TextView? = null
        val textMeaning: TextView? = null
    }
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
       val layoutInflater = LayoutInflater.from(context)
        val newView = layoutInflater.inflate(R.layout.search_list_item, parent, false)
        return newView
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
     val textWord = view?.findViewById<TextView >(R.id.txtWord)
     val textType = view?.findViewById<TextView >(R.id.txtType)
     val textMeaning = view?.findViewById<TextView >(R.id.txtMeaning)

     textWord?.text = cursor?.getString(0)
     textType?.text = cursor?.getString(1)
     textMeaning?.text = cursor?.getString(2)
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