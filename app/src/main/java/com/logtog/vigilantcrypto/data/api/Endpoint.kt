package com.logtog.vigilantcrypto.data.api

import com.logtog.vigilantcrypto.data.model.CoinValue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {
    @GET("/api/{coin}/ticker/")
    fun getCurrencies(@Path(value = "coin", encoded = true) coin : String) : Call<CoinValue>
}