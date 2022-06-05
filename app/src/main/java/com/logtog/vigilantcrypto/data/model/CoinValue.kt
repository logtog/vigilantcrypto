package com.logtog.vigilantcrypto.data.model

data class CoinValue (
    val ticker: Ticker?,
    val cod: String? = null
)

data class Ticker (
    val high: Double? = null,
    val low: Double? = null,
    val vol: Double? = null,
    val last: Double? = null,
    val buy: Double? = null,
    val sell: Double? = null,
    val date: Long? = null,
    val open: Double? = null
)
