package com.logtog.vigilantcrypto.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.logtog.vigilantcrypto.databinding.FragmentHomeBinding
import com.logtog.vigilantcrypto.ui.home.adapter.HomeListAdapter
import com.logtog.vigilantcrypto.ui.presentation.SearchActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val adapter by lazy { HomeListAdapter()}
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


        binding.rvCoins.layoutManager = LinearLayoutManager(activity)

        binding.rvCoins.adapter = adapter

        homeViewModel.listCoin.observe(viewLifecycleOwner) {
            if (homeViewModel.getItemCount() > 0){
                binding.txtInfo.visibility = View.INVISIBLE
            }
            adapter.submitList(it)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireActivity(),SearchActivity::class.java)

            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}