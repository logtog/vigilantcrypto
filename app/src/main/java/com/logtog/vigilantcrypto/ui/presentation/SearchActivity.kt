package com.logtog.vigilantcrypto.ui.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.logtog.vigilantcrypto.R
import com.logtog.vigilantcrypto.data.database.RealTimeDatabase
import com.logtog.vigilantcrypto.data.model.CoinSave
import com.logtog.vigilantcrypto.data.model.CoinSearch
import com.logtog.vigilantcrypto.databinding.ActivitySearchBinding
import com.logtog.vigilantcrypto.ui.home.HomeFragment
import com.logtog.vigilantcrypto.ui.presentation.adapter.SearchListAdapter

@Suppress("CAST_NEVER_SUCCEEDS")
class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    SearchListAdapter.OnItemClickListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var newCoinsList: ArrayList<CoinSearch>
    private lateinit var answer: ArrayList<CoinSearch>
    private lateinit var databaseSave: DatabaseReference
    private lateinit var databaseRead: DatabaseReference
    private lateinit var databaseReadConfig: DatabaseReference
    private val adapter by lazy { SearchListAdapter(this) }
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: String
    private lateinit var coinsSavedList: ArrayList<CoinSave>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth.currentUser?.uid.toString()

        binding.rvCoinsSearch.layoutManager = LinearLayoutManager(this)

        binding.rvCoinsSearch.adapter = adapter

        coinsSavedList = arrayListOf()
        newCoinsList = arrayListOf()


        savedCoinList()
        getCoinsList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        savedCoinList()
        getCoinsList()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        answer = searchCoin(query)

        adapter.submitList(answer)

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == "") {
            savedCoinList()
            getCoinsList()
        } else {

            answer = searchCoin(newText)

            adapter.submitList(answer)
        }
        return false
    }

    private fun searchCoin(text: String?): ArrayList<CoinSearch> {
        val index =
            newCoinsList.indexOfFirst { it.name!!.lowercase() == text || it.cod!!.lowercase() == text }

        return if (index >= 0) arrayListOf(newCoinsList[index]) else arrayListOf()
    }

    private fun savedCoinList() {
        databaseReadConfig = RealTimeDatabase().getDataBaseRealtimeUser()
        databaseReadConfig =
            databaseReadConfig.child(user).child("configurations").child("coinsSaved")

        databaseReadConfig.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                coinsSavedList.clear()
                if (snapshot.exists()) {
                    for (savedSnap in snapshot.children) {
                        val savedData = savedSnap.getValue(String::class.java)
                        val coinSave = CoinSave(savedData!!.toString())
                        coinsSavedList.add(coinSave)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun getCoinsList() {
        databaseRead = RealTimeDatabase().getDataBaseRealtimeCoin()

        binding.rvCoinsSearch.visibility = View.GONE

        databaseRead.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                newCoinsList.clear()
                savedCoinList()

                if (snapshot.exists()) {
                    for (coinSnap in snapshot.children) {
                        val coinData = coinSnap.getValue(CoinSearch::class.java)
                        for (a in coinsSavedList) {
                            if (a.cod == coinData!!.cod) {
                                coinData.saved = true
                            }
                        }
                        newCoinsList.add(coinData!!)
                    }
                    binding.rvCoinsSearch.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                    adapter.submitList(newCoinsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(item: CoinSearch, position: Int) {
        binding.progressBar.visibility = View.VISIBLE
        databaseSave = RealTimeDatabase().getDataBaseRealtimeCoin()

        databaseSave = RealTimeDatabase().getDataBaseRealtimeUser().child(user)
            .child("configurations").child("coinsSaved")

        if (item.saved == true) {
            databaseSave.child(item.cod.toString().lowercase()).setValue(null)
            item.saved = false
        } else {
            item.saved = true
            databaseSave.child(item.cod.toString().lowercase()).setValue(item.cod)
        }
        adapter.notifyDataSetChanged()
        Thread.sleep(250)
        binding.progressBar.visibility = View.GONE
    }
}
