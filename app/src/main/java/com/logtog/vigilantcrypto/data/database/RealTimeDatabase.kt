package com.logtog.vigilantcrypto.data.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RealTimeDatabase {

    fun getDataBaseRealtimeCoin(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://vigilant-crypto-default-rtdb.firebaseio.com").getReference("coins")
    }

    fun getDataBaseRealtimeUser(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://vigilant-crypto-default-rtdb.firebaseio.com").getReference("users")
    }
}

