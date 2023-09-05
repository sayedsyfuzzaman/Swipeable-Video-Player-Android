package com.syfuzzaman.swipeable_video_player_android

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.navigatePopUpTo
import com.syfuzzaman.swipeable_video_player_android.data.navigateTo
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.log

@UnstableApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val date = Date(123, 8, 6)

//        if (bdTime() >= date) {
//            this.finish()
//        }

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
                    navController.navigateTo(
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