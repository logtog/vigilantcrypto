package com.logtog.vigilantcrypto.ui.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.logtog.vigilantcrypto.R
import com.logtog.vigilantcrypto.data.database.RealTimeDatabase
import com.logtog.vigilantcrypto.data.model.CoinSearch
import com.logtog.vigilantcrypto.databinding.ActivitySearchBinding
import com.logtog.vigilantcrypto.ui.presentation.adapter.SearchListAdapter

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var newCoinsList: ArrayList<CoinSearch>
    private lateinit var answer: ArrayList<CoinSearch>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvCoinsSearch.layoutManager = LinearLayoutManager(this)

        newCoinsList = arrayListOf()

        getCoinsList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        answer =  searchCoin(query)

        binding.rvCoinsSearch.adapter = SearchListAdapter(answer)

        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == "") getCoinsList() else {

            answer = searchCoin(newText)

            binding.rvCoinsSearch.adapter = SearchListAdapter(answer)
        }
        return false
    }

    private fun searchCoin(text: String?): ArrayList<CoinSearch> {
        val index = newCoinsList.indexOfFirst { it.name!!.lowercase() == text || it.cod!!.lowercase() == text }

        return if (index >= 0) arrayListOf(newCoinsList[index]) else arrayListOf()
    }

    private fun getCoinsList() {
        database = RealTimeDatabase().getDataBaseRealtime()

        binding.rvCoinsSearch.visibility = View.GONE

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                newCoinsList.clear()

                if (snapshot.exists()) {
                    for (coinSnap in snapshot.children){
                        val coinData = coinSnap.getValue(CoinSearch::class.java)
                        newCoinsList.add(coinData!!)
                    }
                    binding.rvCoinsSearch.adapter = SearchListAdapter(newCoinsList)

                    binding.rvCoinsSearch.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.rvCoinsSearch.adapter = SearchListAdapter(newCoinsList)
    }
}