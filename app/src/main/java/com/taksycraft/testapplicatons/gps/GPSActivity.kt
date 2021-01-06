package com.taksycraft.testapplicatons.gps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityGPSBinding


class GPSActivity : AppCompatActivity()  {

    private lateinit var locationUtil: LocationUtil
    private lateinit var binding: ActivityGPSBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_g_p_s)
        binding.tv.text = "Vinod kumar"
        locationUtil = LocationUtil(this,finishActivity = true){lat, lng ->
            binding.tv.text = lat.toString()+" : "+lng.toString()
        }
        locationUtil.enableMyLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationUtil.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        locationUtil.onResume()
    }

//    override fun onResumeFragments() {
//        super.onResumeFragments()
//        locationUtil.onResume()
//    }

}