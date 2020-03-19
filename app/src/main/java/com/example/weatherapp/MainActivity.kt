package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment? ?: return


        val navController = host.navController

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
        toolBar.setupWithNavController(navController,appBarConfiguration)



    }


}


