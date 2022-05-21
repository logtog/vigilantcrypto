package com.logtog.vigilantcrypto.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.logtog.vigilantcrypto.data.database.RealTimeDatabase
import com.logtog.vigilantcrypto.data.model.Coin
import com.logtog.vigilantcrypto.databinding.FragmentHomeBinding
import com.logtog.vigilantcrypto.ui.home.adapter.HomeListAdapter
import com.logtog.vigilantcrypto.ui.presentation.CoinPageActivity
import com.logtog.vigilantcrypto.ui.presentation.SearchActivity

class HomeFragment : Fragment(), HomeListAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var database : DatabaseReference
    private val adapter by lazy { HomeListAdapter(this)}
    private lateinit var firebaseAuth: FirebaseAuth
    private val binding get() = _binding!!


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

        binding.rvCoins.layoutManager = LinearLayoutManager(activity)

        binding.rvCoins.adapter = adapter


       /* homeViewModel.listCoin.observe(viewLifecycleOwner) {
            if (homeViewModel.getItemCount() > 0){
                binding.txtInfo.visibility = View.INVISIBLE
            }
            adapter.submitList(it)
        }
        */
        getCoinList()



        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireActivity(),SearchActivity::class.java)

            startActivity(intent)

        }

        return root
    }

    private fun getCoinList() {

        binding.txtInfo.visibility = View.INVISIBLE

        database = RealTimeDatabase().getDataBaseRealtimeCoin()
        val array = ArrayList<Coin>()

        binding.rvCoins.visibility = View.GONE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()

                if (snapshot.exists()) {
                    for (coinSnap in snapshot.children){
                        val coinData = coinSnap.getValue(Coin::class.java)

                        array.add(coinData!!)
                    }
                    adapter.submitList(array)
                    binding.rvCoins.visibility = View.VISIBLE
                    binding.progressBar2.visibility = View.GONE
                }
                if (adapter.itemCount == 0){
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
        intent.putExtra("COD",item.cod.toString())
        intent.putExtra("NAME",item.name.toString())
        intent.putExtra("PRICE",item.price.toString())
        intent.putExtra("IMAGECOIN",item.imageCoin.toString())
        intent.putExtra("PERCENTAGE",item.percentage.toString())
        intent.putExtra("TIMEALERT",item.timeAlert.toString())
        startActivity(intent)
}
}