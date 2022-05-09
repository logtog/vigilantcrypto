package com.logtog.vigilantcrypto.ui.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logtog.vigilantcrypto.R
import com.logtog.vigilantcrypto.data.model.CoinSearch

class SearchListAdapter(private val coinsList: ArrayList<CoinSearch>) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cod: TextView = itemView.findViewById(R.id.tv_cod_search)
        val name: TextView = itemView.findViewById(R.id.tv_name_search)
        val imageCoin: ImageView = itemView.findViewById(R.id.iv_coin_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = coinsList[position]
        holder.cod.text = currentItem.cod
        holder.name.text = currentItem.name
        Glide.with(holder.itemView.context).load(currentItem.imageCoin).into(holder.imageCoin)
    }

    override fun getItemCount(): Int {
        return coinsList.size
    }
}