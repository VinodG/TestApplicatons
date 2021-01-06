package com.taksycraft.testapplicatons.gps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationUtil(var context: Context, var finishActivity: Boolean = true, var listener: (Double,Double) -> Unit) {
    private var fusedLocationClient: FusedLocationProviderClient
    private var permissionDenied = false
    companion object{
        const val REQUEST_CHECK_LOCATION_SETTINGS: Int = 5000
        const val LOCATION_PERMISSION_REQUEST_CODE =5001
    }
    init{
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            createLocationRequest()
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((context as AppCompatActivity), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, finishActivity)
        }
    }
    fun createLocationRequest() {
        LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(this)
            val client: SettingsClient = LocationServices.getSettingsClient(context)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener { locationSettingsResponse ->
                // All location settings are satisfied. The client can initialize
                // location requests here.
                getLastLocation(this,context)
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        Toast.makeText(context, "Permission are not given", Toast.LENGTH_SHORT).show()
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult((context as AppCompatActivity),
                                REQUEST_CHECK_LOCATION_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        sendEx.printStackTrace()                    }
                }
            }
        }

    }
    private fun getLastLocation(locationRequest: LocationRequest,context:Context) {
        if (ActivityCompat.checkSelfPermission(context as AppCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(context as AppCompatActivity, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, finishActivity);

            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,  object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val locationList = locationResult.locations
                if (locationList.size > 0) {
                    //The last location in the list is the newest
                    val location = locationList[locationList.size - 1]
                    listener.invoke(location.latitude,location.longitude)
                    var str= "${location.latitude} : ${location.longitude}"
                    Log.i("MapsActivity", "Location: " +str )
                    println(str)
                }
            }
        }, Looper.myLooper());
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == LocationUtil.REQUEST_CHECK_LOCATION_SETTINGS ||  requestCode == PermissionUtils.REQUEST_APP_INFO ){
            enableMyLocation()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode != LocationUtil.LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    fun onResume() {
        if (permissionDenied) {
            /**
             * Permission was not granted, display error dialog.
             * Displays a dialog with error message explaining that the location permission is missing.
             */
            PermissionUtils.PermissionDeniedDialog.newInstance(finishActivity).show((context as AppCompatActivity).supportFragmentManager, "dialog")
            permissionDenied = false
        }
    }

}