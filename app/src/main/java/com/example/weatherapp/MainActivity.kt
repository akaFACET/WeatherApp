package com.example.weatherapp

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var preferencesManager: PreferencesManager


    init {
        preferencesManager = PreferencesManager(App.instance)
    }


    override fun attachBaseContext(newBase: Context?) {
        Log.e("LANG", "BaseContextAttached")
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(newBase!!,
            Locale(preferencesManager.getSavedLanguage(),preferencesManager.getSavedCountry())))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        RuntimeLocaleChanger.overrideLocale(this,Locale(preferencesManager.getSavedLanguage(),preferencesManager.getSavedCountry()))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment? ?: return


        navController = host.navController

        val sideBar = findViewById<NavigationView>(R.id.nav_view)

        sideBar?.setupWithNavController(navController)

//        sideBar.setNavigationItemSelectedListener { menu ->
//            when (menu.itemId) {
//                R.id.savedWeatherFragment -> {
//                    sideBar.menu.findItem(menu.itemId).setEnabled(false)
//                }
//
//            }
//            false
//        }

        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout = drawer_layout)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                R.id.savedWeatherFragment -> {
                    navController.navigate(R.id.savedWeatherFragment)
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                else -> {
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
                }

        }
        setSupportActionBar(toolBar)
        toolBar.setupWithNavController(navController,appBarConfiguration)


    }


}



