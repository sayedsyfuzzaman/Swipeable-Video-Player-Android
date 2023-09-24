package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nexdecade.nd_shorts.utils.navigatePopUpTo
import com.nexdecade.nd_shorts.utils.navigateTo
import com.syfuzzaman.swipeable_video_player_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@UnstableApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel by viewModels<HomeViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel.autoPlayShorts.value = null
        val date = Date(123, 9, 3)

        if (bdTime() >= date) {
            Toast.makeText(this, "Demo Expired", Toast.LENGTH_LONG).show()
            this.finish()
        }
        binding.uploadButton.setOnClickListener {
            Toast.makeText(this, "Action Required", Toast.LENGTH_SHORT).show()
        }
        setupNavController()
    }

    private fun setupNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)


        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigatePopUpTo(
                        resId = R.id.homeFragment
                    )
                    true
                }

                R.id.shortsFragment -> {
                    navController.navigateTo(
                        resId = R.id.shortsFragment
                    )
                    true
                }

                R.id.subscriptionFragment -> {
                    Toast.makeText(this, "Action Required", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.libraryFragment -> {
                    Toast.makeText(this, "Action Required", Toast.LENGTH_SHORT).show()
                    true
                }
                // Add cases for other menu items as needed
                else -> false
            }
        }
    }


    private fun bdTime(): Date {
        return try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"))
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka"))
            cal.time
        } catch (e: Exception) {
            Date()
        }
    }

    val currentDateTime: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            return sdf.format(bdTime()) ?: sdf.format(Date())
        }

    val currentDateTimeMillis: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH)
            return sdf.format(bdTime()) ?: sdf.format(Date())
        }

    override fun onPause() {
        super.onPause()
        Log.d("nd_shorts", "onPause: sd")
    }
}