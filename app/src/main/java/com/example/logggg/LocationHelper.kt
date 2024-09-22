import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentLocation(onComplete: (Double, Double) -> Unit, onFailure: (String) -> Unit) {
        if (hasLocationPermission()) {
            try {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location != null) {
                        onComplete(location.latitude, location.longitude)
                    } else {
                        onFailure("Location not available")
                    }
                }
            } catch (e: SecurityException) {
                onFailure("Location permission is required")
            }
        } else {
            onFailure("Location permission not granted")
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
