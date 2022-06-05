package com.logtog.vigilantcrypto.ui.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logtog.vigilantcrypto.R
import com.logtog.vigilantcrypto.data.model.CoinSearch
import com.logtog.vigilantcrypto.databinding.CoinItemBinding


class SearchListAdapter(private var clickListener: OnItemClickListener) :
    ListAdapter<CoinSearch, SearchListAdapter.ViewHolder>(DiffCallback()){

    inner class ViewHolder(private val binding: CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun initialize (item: CoinSearch, action: OnItemClickListener) {

                binding.tvCodSearch.text = item.cod
                binding.tvNameSearch.text = item.name
                Glide.with(binding.root.context).load(item.imageCoin)
                    .into(binding.ivCoinSearch)

                itemView.setOnClickListener {
                    action.onItemClick(item,bindingAdapterPosition)
                }

                if (item.saved == true) {
                    binding.ivAddSearch.setBackgroundResource(R.drawable.ic_baseline_remove_50)
                } else {
                    binding.ivAddSearch.setBackgroundResource(R.drawable.ic_baseline_add_50)
                }
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CoinItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initialize(getItem(position),clickListener)
    }


    interface OnItemClickListener{
        fun onItemClick(item: CoinSearch, position: Int)
    }
}

class DiffCallback: DiffUtil.ItemCallback<CoinSearch>() {
    override fun areItemsTheSame(oldItem: CoinSearch, newItem: CoinSearch) = oldItem == newItem
    override fun areContentsTheSame(oldItem: CoinSearch, newItem: CoinSearch) = oldItem == newItem
}