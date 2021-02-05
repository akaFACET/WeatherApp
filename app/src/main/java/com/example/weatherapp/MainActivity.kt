  package com.example.weatherapp

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.Utils.LocaleChanger
import com.example.weatherapp.data.PreferencesManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    @Inject
    lateinit var preferencesManager: PreferencesManager


    init {
       // preferencesManager = PreferencesManager(App.instance)
//        App.get(this).applicationComponent.inject(this)
//        (application as App).applicationComponent.inject(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        //(application as App).applicationComponent.inject(this)
        App.get(newBase!!).applicationComponent.inject(this)
        super.attachBaseContext(
            LocaleChanger.wrapContext(newBase!!,
            Locale(preferencesManager.getSavedLanguage(),preferencesManager.getSavedCountry())))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.overrideLocale(this,Locale(preferencesManager.getSavedLanguage(),preferencesManager.getSavedCountry()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment? ?: return

        navController = host.navController

        val sideBar = findViewById<NavigationView>(R.id.nav_view)

        sideBar?.setupWithNavController(navController)

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



