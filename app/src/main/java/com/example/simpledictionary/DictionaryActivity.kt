package com.example.simpledictionary

import android.app.SearchManager

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import com.example.simpledictionary.databinding.ActivityMainBinding


class DictionaryActivity : AppCompatActivity() {
    companion object {
        val LAST_SEARCH_WORD: String = "LAST_SEARCH_WORD"
    }

    var mDbHelper: DataBaseHelper? = null
    var mSearchListAdapter: SearchListAdapter? = null
    var mSearchQuery: String = ""

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDbHelper = DataBaseHelper(applicationContext)
        mSearchQuery = savedInstanceState?.getString(LAST_SEARCH_WORD) ?: ""

        showDictUI()
    }

    private fun showDictUI() {
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        mSearchListAdapter =
            SearchListAdapter(applicationContext, mDbHelper!!.getWords(mSearchQuery))
        val lstWords = findViewById<ListView>(R.id.lstWords)
        lstWords.adapter = mSearchListAdapter

        lstWords.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val wordDetailIntent = Intent(applicationContext, WordDetailActivity::class.java)
                wordDetailIntent.putExtra(WordDetailActivity.WORD_ID, "$id")
                startActivity(wordDetailIntent)
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString(LAST_SEARCH_WORD, mSearchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mSearchQuery = savedInstanceState?.getString(LAST_SEARCH_WORD) ?: ""
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action.equals(Intent.ACTION_SEARCH)) {
            val searchQuery = intent?.getStringExtra(SearchManager.QUERY) ?: ""
            Log.d("DictionaryActivity", "searchQuery = $searchQuery")
//            mSearchListAdapter?.changeCursor(mDbHelper!!.getWords(searchQuery))
            updateListByQuery(searchQuery)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView: SearchView? = menu.findItem(R.id.action_search).actionView as? SearchView
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))



        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                mSearchListAdapter?.changeCursor(mDbHelper!!.getWords(newText?: ""))

                updateListByQuery(newText ?: "")

                return true
            }
        })
        return true
    }

    private fun updateListByQuery(searchQuery: String) {
        mSearchQuery = searchQuery
        mSearchListAdapter?.changeCursor(mDbHelper!!.getWords(searchQuery))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                onSearchRequested()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}