package com.logtog.vigilantcrypto.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.logtog.vigilantcrypto.data.model.Coin

class HomeViewModel : ViewModel() {

    private val coin : ArrayList<Coin>get(){
        val array = ArrayList<Coin>()
        val btc1 = Coin("BTC","Bitcoin","https://cryptologos.cc/logos/bitcoin-btc-logo.png",189545.50,"Desativado",45)

        array.add(btc1)
        return array
    }

    private val _coin = MutableLiveData<List<Coin>>().apply {
        value = coin
    }
    val listCoin: MutableLiveData<List<Coin>> = _coin
}