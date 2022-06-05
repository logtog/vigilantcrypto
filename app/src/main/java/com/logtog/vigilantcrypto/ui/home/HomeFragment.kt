package com.logtog.vigilantcrypto.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.logtog.vigilantcrypto.data.api.Endpoint
import com.logtog.vigilantcrypto.data.database.RealTimeDatabase
import com.logtog.vigilantcrypto.data.model.Coin
import com.logtog.vigilantcrypto.data.model.CoinSave
import com.logtog.vigilantcrypto.data.model.CoinValue
import com.logtog.vigilantcrypto.data.util.NetworkUtils
import com.logtog.vigilantcrypto.databinding.FragmentHomeBinding
import com.logtog.vigilantcrypto.ui.home.adapter.HomeListAdapter
import com.logtog.vigilantcrypto.ui.presentation.CoinPageActivity
import com.logtog.vigilantcrypto.ui.presentation.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class HomeFragment : Fragment(), HomeListAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private var shouldRefreshOnResume = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var database: DatabaseReference
    private val adapter by lazy { HomeListAdapter(this) }
    private lateinit var firebaseAuth: FirebaseAuth
    private val binding get() = _binding!!
    private lateinit var user: String
    private lateinit var readDataBaseConfig: DatabaseReference
    private lateinit var arraySaved: ArrayList<CoinSave>
    private val coinValue: MutableCollection<CoinValue> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth.uid.toString()


        /* homeViewModel.listCoin.observe(viewLifecycleOwner) {
             if (homeViewModel.getItemCount() > 0){
                 binding.txtInfo.visibility = View.INVISIBLE
             }
             adapter.submitList(it)
         }
         */
        binding.txtInfo.visibility = View.INVISIBLE
        binding.rvCoins.visibility = View.GONE

        arraySaved = arrayListOf()
        initAdapter()
        getSavedList()


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            shouldRefreshOnResume = true
            startActivity(intent)
        }


        return root
    }

    override fun onResume() {
        super.onResume()
        // Check should we need to refresh the fragment
        if (shouldRefreshOnResume) {
            requireActivity().recreate()
        }
        if (adapter.itemCount == 0) {
            binding.txtInfo.visibility = View.VISIBLE
            binding.progressBar2.visibility = View.INVISIBLE
        }
    }

    private fun initAdapter() {
        binding.rvCoins.layoutManager = LinearLayoutManager(activity)
        binding.rvCoins.adapter = adapter
    }

    private fun getSavedList() {
        readDataBaseConfig = RealTimeDatabase().getDataBaseRealtimeUser()
        readDataBaseConfig =
            readDataBaseConfig.child(user).child("configurations").child("coinsSaved")

        readDataBaseConfig.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arraySaved.clear()
                if (snapshot.exists()) {
                    coinValue.clear()
                    for (savedSnap in snapshot.children) {
                        val savedData = savedSnap.getValue(String::class.java)
                        val coinSave = CoinSave(savedData!!.toString())
                        arraySaved.add(coinSave)
                        createData(coinSave.cod.toString())
                    }

                    getCoinList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun createData(coin : String) {
        val retrofitClient = NetworkUtils.getRetrofitInstance("https://www.mercadobitcoin.net")
        val endpoint = retrofitClient.create(Endpoint::class.java)


        endpoint.getCurrencies(coin).enqueue(object : Callback<CoinValue> {
            override fun onResponse(call: Call<CoinValue>, response: Response<CoinValue>) {
                if (response.isSuccessful) {
                     saveValuesArray(CoinValue(response.body()?.ticker!!,coin))
                }
            }

            override fun onFailure(call: Call<CoinValue>, t: Throwable) {
                Toast.makeText(activity, "Error in getting data from api.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        Thread.sleep(250)
    }


    private fun saveValuesArray(coin : CoinValue){
        coinValue.add(coin)
    }

    private fun getCoinList() {
        val decimalFormat = DecimalFormat("0.00")
        binding.txtInfo.visibility = View.INVISIBLE
        binding.rvCoins.visibility = View.GONE

        database = RealTimeDatabase().getDataBaseRealtimeCoin()

        val array = ArrayList<Coin>()

        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                if (snapshot.exists()) {
                    for (coinSnap in snapshot.children) {
                        val coinData = coinSnap.getValue(Coin::class.java)
                        for (a in arraySaved) {
                            if (coinData!!.cod == a.cod) {
                                for (x in coinValue){
                                    if (x.cod == coinData.cod){
                                        val open = x.ticker!!.open!!.toDouble()
                                        val last = x.ticker.last!!.toDouble()

                                        val percentage = (last - open) / open * 100

                                        coinData.percentage = decimalFormat.format(percentage).toString()

                                        coinData.price = decimalFormat.format(x.ticker.last)
                                    }
                                }
                                array.add(coinData)
                            }
                        }
                    }
                    adapter.submitList(array)
                    adapter.notifyDataSetChanged()
                    binding.rvCoins.visibility = View.VISIBLE
                    binding.progressBar2.visibility = View.GONE
                }
                if (adapter.itemCount == 0) {
                    binding.txtInfo.visibility = View.VISIBLE
                    binding.progressBar2.visibility = View.INVISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Coin, position: Int) {
        val intent = Intent(requireActivity(), CoinPageActivity::class.java)
        intent.putExtra("COD", item.cod.toString())
        intent.putExtra("NAME", item.name.toString())
        intent.putExtra("PRICE", item.price.toString())
        intent.putExtra("IMAGECOIN", item.imageCoin.toString())
        intent.putExtra("PERCENTAGE", item.percentage.toString())
        intent.putExtra("TIMEALERT", item.timeAlert.toString())
        startActivity(intent)
    }
}