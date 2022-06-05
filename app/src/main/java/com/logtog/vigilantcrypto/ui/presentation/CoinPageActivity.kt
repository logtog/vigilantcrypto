package com.logtog.vigilantcrypto.ui.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.logtog.vigilantcrypto.databinding.ActivityCoinPageBinding

class CoinPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar3)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val price = "R$ " + intent.getStringExtra("PRICE")
        var percentage = intent.getStringExtra("PERCENTAGE")

        if (percentage?.toDouble()!! < 0) {
            binding.tvPercentagePage.setTextColor(Color.RED)
        }

        percentage = "$percentage%"

        binding.tvNamePage.text = intent.getStringExtra("NAME")
        binding.tvPricePage.text = price
        binding.tvPercentagePage.text = percentage
        Glide.with(binding.root.context)
            .load(intent.getStringExtra("IMAGECOIN").toString()).into(binding.ivCoinPage)

        binding.ivAlertPage.setOnClickListener {
            val intent = Intent(this, NotificationConfigActivity::class.java)

            startActivity(intent)
        }
    }
}