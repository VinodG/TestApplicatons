package com.taksycraft.testapplicatons.camera_gallery

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityGalleryCameraBinding

class GalleryCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryCameraBinding
    var pickMediaActivity: PickMediaActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_camera)
        pickMediaActivity = PickMediaActivity(this, listen ={ s: String, bitmap: Bitmap?, s1: String? -> binding.iv.setImageBitmap(bitmap) })
        binding.btnOpen.setOnClickListener { openCamera() }
        openCamera()
        //        pickMediaActivity.requestPermissions();
    }

    private fun openCamera() {
        pickMediaActivity?.let {
            it.checkPermission(this, PERMISSIONS_CAMERA, PickMediaActivity.cameraNeverAskAgain)
        }
    }

    override fun onResume() {
        super.onResume()
//        val checker = PermissionsChecker(this)
//        if (!checker.lacksPermissions(*PERMISSIONS_CAMERA)) {
//            pickMediaActivity?.let { it.SetToSharePreference(this, PickMediaActivity.cameraNeverAskAgain, false) }
//        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent ?) {
        super.onActivityResult(requestCode, resultCode, data)
        pickMediaActivity?.let {
            data?.let { d -> it.activityResult(this, PickMediaActivity.my_profile, requestCode, resultCode, d, true) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        try {
            pickMediaActivity?.let {
                it.activityPermissionsResult(this, requestCode, permissions, grantResults)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        var PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}