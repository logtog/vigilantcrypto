package com.logtog.vigilantcrypto.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.logtog.vigilantcrypto.data.model.Coin

class HomeViewModel : ViewModel() {

    private val coin : ArrayList<Coin>get(){
        val array = ArrayList<Coin>()
        //val cripto1 = Coin("BTC","Bitcoin","https://cryptologos.cc/logos/bitcoin-btc-logo.png",189545.50,"Desativado",45)
        //val cripto2 =  Coin("ETH","Ethereum","https://cryptologos.cc/logos/ethereum-eth-logo.png",189545.50,"Desativado",45)
        //array.add(cripto1)
        //array.add(cripto2)
        return array
    }

    private val _coin = MutableLiveData<List<Coin>>().apply {
        value = coin
    }
    val listCoin: MutableLiveData<List<Coin>> = _coin

    fun getItemCount(): Int {
        return coin.size
    }
}
