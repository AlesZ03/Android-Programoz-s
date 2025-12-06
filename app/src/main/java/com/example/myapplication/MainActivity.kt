package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View // Importáld a View-t
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: MainActivity created.")

        // NavController beállítása
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // BottomNavigationView összekötése a NavController-rel
        binding.bottomNav.setupWithNavController(navController)



        // Listener hozzáadása a navigációs célpontok figyelésére
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Itt add meg azokat a fragment ID-kat, ahol a menünek látszódnia kell.
            // Ezeket az ID-kat a res/navigation/nav_graph.xml fájlban találod.
            when (destination.id) {
                R.id.homeFragment,
                R.id.profileFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                // Minden más esetben (pl. a LoginFragment-nél) a menü rejtett lesz.
                else -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: MainActivity started.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: MainActivity resumed.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: MainActivity paused.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: MainActivity stopped.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: MainActivity restarted.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: MainActivity destroyed.")
    }
}
