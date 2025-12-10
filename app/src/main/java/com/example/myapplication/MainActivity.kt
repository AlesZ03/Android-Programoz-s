package com.example.myapplication


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
// ⬇️ 1. LÉPÉS: Importáld a generált osztályt ⬇️
import com.example.myapplication.NavGraphDirections
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.SessionManager

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: MainActivity created.")

        sessionManager = SessionManager(applicationContext)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.profileFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }

        // ⬇️ Itt egy felesleges sor volt, töröld ki ⬇️
        // 🔹 ÚJ LOG HOZZÁADÁSA 🔹        val token = sessionManager.fetchAuthToken()

        binding.root.post {
            val token = sessionManager.fetchAuthToken()
            Log.d("MainActivity", "Induláskori token ellenőrzés. Talált token: $token")

            if (token == null) {
                Log.d("MainActivity", "Nincs mentett token, navigálás a LoginFragment-re.")

                // ⬇️ 2. LÉPÉS: ÍGY HIVATKOZZ AZ ACTION-RE ⬇️
                // Ez a metódus a NavGraphDirections osztályból jön.
                val action = NavGraphDirections.actionGlobalToLoginFragment()
                navController.navigate(action)

            } else {
                Log.d("MainActivity", "Van mentett token, a HomeFragment-en maradunk.")
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
