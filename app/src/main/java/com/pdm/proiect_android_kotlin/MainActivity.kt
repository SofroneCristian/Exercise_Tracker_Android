package com.pdm.proiect_android_kotlin


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.pdm.proiect_android_kotlin.databinding.ActivityMain2Binding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding

    companion object {
        lateinit var email: String
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_NOTIFICATION_PERMISSION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_map, R.id.nav_add_exercises, R.id.nav_music_category
            ), drawerLayout
        )

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_map -> {
                    val mapFragment = MapFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, mapFragment)
                        .addToBackStack(null)
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add_exercises -> {
                    val addExercisesFragment = AddExercisesFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, addExercisesFragment)
                        .addToBackStack(null)
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_music_category -> {
                    val musicCategoryFragment = MusicCategoryFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, musicCategoryFragment)
                        .addToBackStack(null)
                        .commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        email = intent.getStringExtra("email") ?: "No Email"
        setNavigationHeaderEmail(email)

        requestLocationPermissions()
        requestNotificationPermissions() // Cere permisiuni pentru notificări
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setNavigationHeaderEmail(email: String) {
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.userEmail)
        userEmailTextView.text = email
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun requestNotificationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.FOREGROUND_SERVICE),
                REQUEST_NOTIFICATION_PERMISSION
            )
        } else {
            startNotificationService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                    fragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
                } else {
                    Snackbar.make(binding.root, "Location permission denied", Snackbar.LENGTH_LONG).show()
                }
            }
            REQUEST_NOTIFICATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startNotificationService()
                } else {
                    Snackbar.make(binding.root, "Notification permission denied", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startNotificationService() {
        val intent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }
}
