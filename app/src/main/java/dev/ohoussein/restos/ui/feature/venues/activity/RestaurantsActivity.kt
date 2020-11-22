package dev.ohoussein.restos.ui.feature.venues.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.ohoussein.restos.databinding.ActivityRestaurantsBinding

@AndroidEntryPoint
class RestaurantsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
