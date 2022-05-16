package com.logtog.vigilantcrypto.data.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RealTimeDatabase {

    fun getDataBaseRealtime(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://vigilant-crypto-default-rtdb.firebaseio.com").getReference("coins")
    }

}

