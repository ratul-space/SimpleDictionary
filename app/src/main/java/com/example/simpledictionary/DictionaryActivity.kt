package com.example.simpledictionary

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import com.example.simpledictionary.databinding.ActivityMainBinding


class DictionaryActivity : AppCompatActivity() {
    var mDbHelper: DataBaseHelper? = null
    var mSearchListAdapter: SearchListAdapter? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(binding.toolbar)

        mDbHelper = DataBaseHelper(applicationContext)
//        dbHelper.addSomeDummyWords()
//        dbHelper.getWords()
        mSearchListAdapter = SearchListAdapter(applicationContext, mDbHelper!!.getWords())
        val lstWords = (findViewById<ListView>(R.id.lstWords))
        lstWords.adapter = mSearchListAdapter

        lstWords.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,

                ) {
                Log.d("DictionaryActivity", "$parent\n $view\n $position\n $id")

                val wordDetailIntent = Intent(applicationContext, WordDetailActivity::class.java)
                wordDetailIntent.putExtra(WordDetailActivity.WORD_ID, "$id")
                startActivity(wordDetailIntent)
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action.equals(Intent.ACTION_SEARCH)) {
            val searchQuery = intent?.getStringExtra(SearchManager.QUERY) ?: ""
            Log.d("DictionaryActivity", "searchQuery = $searchQuery")
            mSearchListAdapter?.changeCursor(mDbHelper!!.getWords(searchQuery))
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

                mSearchListAdapter?.changeCursor(mDbHelper!!.getWords(newText ?: ""))

                return true
            }
        })
        return true
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