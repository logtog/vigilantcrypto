package com.logtog.vigilantcrypto.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logtog.vigilantcrypto.data.model.Coin
import com.logtog.vigilantcrypto.databinding.AlertItemBinding

class HomeListAdapter : ListAdapter<Coin, HomeListAdapter.ViewHolder>(DiffCallback()){


    inner class ViewHolder(private val binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Coin){
            val percentage = "%" + item.percentage.toString()
            val price = "R$ " + item.price.toString()
            binding.tvCoinName.text = item.name
            binding.tvCoinCod.text = item.cod
            binding.tvTime.text = item.timeAlert.toString()
            binding.tvPercentage.text = percentage
            binding.tvPrice.text = price
            Glide.with(binding.root.context)
                .load(item.imageCoin).into(binding.ivCoin)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlertItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
class DiffCallback: DiffUtil.ItemCallback<Coin>() {
    override fun areItemsTheSame(oldItem: Coin, newItem: Coin) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Coin, newItem: Coin) = oldItem == newItem
}