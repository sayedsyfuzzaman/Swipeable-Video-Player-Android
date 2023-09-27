package com.nexdecade.nd_shorts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nexdecade.nd_shorts.data.ShortsBean
import com.nexdecade.nd_shorts.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class ShortsActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shorts)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val extras = intent.extras
        if (extras != null) {
            val playingShortsList = extras.getSerializable("shortsList") as ArrayList<*>
            navController.navigateTo(R.id.shortsFragment, args = bundleOf("shorts" to playingShortsList))
        }
        else{
            navController.navigateTo(R.id.shortsFragment)
        }
    }
}