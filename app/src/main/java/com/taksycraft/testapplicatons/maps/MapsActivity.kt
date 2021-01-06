package com.taksycraft.testapplicatons.maps

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityMapsBinding
import java.lang.StringBuilder

class MapsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsBinding
    lateinit var map:GoogleMap
    var sydney = LatLng(-34.0, 151.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.tvCentrePoint.setOnClickListener {
            var latlng = map.cameraPosition.target
            addMarker(latlng,this.map)
        }
    }
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        this.map = googleMap
        getLocationFromAddress("police station, sircilla")?.let {
            sydney =it
            addMarker(sydney, googleMap)
        }
        getLocationListFromAddress("hyderabad")?.let {
            sydney =it
            addMarker(sydney, googleMap)
        }


    }

    private fun addMarker(latLng: LatLng, googleMap: GoogleMap?) {
        googleMap?.addMarker(MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Marker in Sydney"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun toast(str: String) {
        Toast.makeText(this ,
                str,
                Toast.LENGTH_LONG).show();

    }
    public fun getLocationFromAddress(strAddress : String ):LatLng?{
        val address : List<Address>
        try {
            Geocoder(this).getFromLocationName(strAddress, 10)?.let {
                 address = it
                address[0].locality
                 return LatLng(address.get(0).latitude,address.get(0).longitude)
             } ?: null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    public fun getLocationListFromAddress(strAddress : String ):LatLng?{
        val address : List<Address>
        try {
            Geocoder(this).getFromLocationName(strAddress, 5)?.let {
                 address = it
                var addrs  =  StringBuilder()
                (0 until address.size).forEach {
                    (0..address[it].maxAddressLineIndex).forEach {  i ->
                        addrs.append(address[it].getAddressLine(i)+",- ")
                    }
                    addrs.append('\n')
                }
                println(addrs.toString())
                 return LatLng(address.get(0).latitude,address.get(0).longitude)
             } ?: null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}