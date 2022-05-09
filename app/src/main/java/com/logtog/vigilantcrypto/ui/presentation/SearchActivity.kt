package com.logtog.vigilantcrypto.ui.presentation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.logtog.vigilantcrypto.R
import com.logtog.vigilantcrypto.data.model.CoinSearch
import com.logtog.vigilantcrypto.databinding.ActivitySearchBinding
import com.logtog.vigilantcrypto.ui.presentation.adapter.SearchListAdapter

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var newCoinsList: ArrayList<CoinSearch>
    private lateinit var answer: ArrayList<CoinSearch>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvCoinsSearch.layoutManager = LinearLayoutManager(this)

        getCoinsList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        newCoinsList.forEach {

            answer = if (it.name == query.toString() || it.cod == query.toString()){
                arrayListOf(it)

            } else {
                Log.e("TAG", "Não FOI" )
                arrayListOf()
            }
        }
        Log.e("TAG",answer.toString() )
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        newCoinsList.forEach {
            if (it.name == newText || it.cod == newText){
                binding.rvCoinsSearch.adapter = SearchListAdapter(arrayListOf(it))
            }
        }
        if(newText == "") getCoinsList()
        return false
    }

    private fun getCoinsList() {
        val btc = CoinSearch("BTC","Bitcoin","https://cryptologos.cc/logos/bitcoin-btc-logo.png")
        val eth = CoinSearch("ETH","Ethereum","https://cryptologos.cc/logos/ethereum-eth-logo.png")
        newCoinsList = arrayListOf()
        newCoinsList.add(btc)
        newCoinsList.add(eth)

        binding.rvCoinsSearch.adapter = SearchListAdapter(newCoinsList)
    }
}