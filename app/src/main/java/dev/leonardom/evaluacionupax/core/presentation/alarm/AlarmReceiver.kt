package dev.leonardom.evaluacionupax.core.presentation.alarm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.leonardom.evaluacionupax.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


/*
    Clase que extiende de BroadcastReceiver para programar alarma y ejecutar bloque de código
    de forma periódica cada 5 minutos con aplicación en primer plano o segundo plano
 */
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    private val channelName = "channelName"
    private val channelId = "channelId"

    private lateinit var notification: Notification
    private val notificationID = 0

    @Inject
    lateinit var locationReference: CollectionReference

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var timestamp: Timestamp = Timestamp.now()
    private var locationHashMap: HashMap<String, Any> = hashMapOf()

    // Código a ejecutar cuando la alarma es activada
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // Log.d("AlarmReceiver", "Location = Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                latitude = location.latitude
                longitude =location.longitude
                timestamp = Timestamp.now()

                locationHashMap = hashMapOf(
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "timestamp" to timestamp
                )

                locationReference.add(locationHashMap)
            }

            createNotificationChannel(context)

            buildNotification(context)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationID, notification)
        }
    }

    private fun buildNotification(context: Context) {
        notification = NotificationCompat.Builder(context, channelId).also {
            it.setSmallIcon(R.drawable.ic_notifications)
            it.setContentTitle(context.getString(R.string.notification_title))
            it.setContentText(context.getString(R.string.notification_description))
            it.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }.build()
    }

    private fun createNotificationChannel(context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
                lightColor = Color.RED
                enableLights(true)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}