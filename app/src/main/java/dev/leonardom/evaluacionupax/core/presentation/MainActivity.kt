package dev.leonardom.evaluacionupax.core.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.leonardom.evaluacionupax.R
import dev.leonardom.evaluacionupax.core.presentation.alarm.AlarmReceiver
import dev.leonardom.evaluacionupax.core.util.showSnackbar
import dev.leonardom.evaluacionupax.databinding.ActivityMainBinding

/*
    Implementación de Arquitectura de una sola actividad, MainActivity funciona como
    componente para alojar y manejar las diferentes pantallas(fragmentos) de la aplicación
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layout: View
    private lateinit var navController: NavController
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    /*
        Contrato para la validación de los permisos de Localización
     */
    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted) {
            setupAlarmManager()
        } else {
            requestLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layout = binding.mainLayout

        // Configurción de BottomNavigation con librería de NavigationComponents
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)

        requestLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /*
        Revisar estado de los permisos y realizar acción acorde a cada uno
     */
    private fun requestLocationPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                setupAlarmManager()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission
            ) -> {
                layout.showSnackbar(
                    binding.navHostFragment,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok_button),
                    anchorView = binding.bottomNavigationView
                ) {
                    requestLocationPermissionLauncher.launch(permission)
                }
            }

            else -> {
                requestLocationPermissionLauncher.launch(permission)
            }
        }
    }

    /*
        Configuración de AlarmManager para trabajo periódico
     */
    private fun setupAlarmManager() {
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val interval = (1000 * 60 * 5).toLong() // 5 minutos

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + interval,
            interval,
            pendingIntent
        )
    }
}