package com.geekymusketeers.medify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medify.R
import com.example.medify.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the action bar
        supportActionBar?.hide()

        // Setup Bottom Navigation
        val bottomNavigationView = binding.bottomNav
        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home, R.id.stats, R.id.appointment, R.id.settings)
        )

        // Connect Bottom Navigation with NavController
        bottomNavigationView.setupWithNavController(navController)
    }
}
