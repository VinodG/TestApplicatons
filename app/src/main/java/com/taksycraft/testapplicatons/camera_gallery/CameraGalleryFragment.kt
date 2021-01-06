package com.taksycraft.testapplicatons.camera_gallery

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.FragmentCameraGalleryBinding

class CameraGalleryFragment : Fragment() {

    private lateinit var pickMediaActivity: PickMediaActivity
    private lateinit var binding: FragmentCameraGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_camera_gallery, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity()).load("/storage/emulated/0/TestApplicatons/profile_bayat.jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.iv)

        pickMediaActivity = PickMediaActivity(requireActivity(), this, listen ={ s: String, bitmap: Bitmap?, url: String? ->
            Glide.with(requireActivity()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.iv)
            println(url)
        })
        binding.btnOpen.setOnClickListener { openCamera() }
        openCamera()
    }
    private fun openCamera() {
        pickMediaActivity?.let {
            it.checkPermission(requireContext(), PERMISSIONS_CAMERA, PickMediaActivity.cameraNeverAskAgain)
        }
    }

    override fun onResume() {
        super.onResume()
//        val checker = PermissionsChecker(this)
//        if (!checker.lacksPermissions(*PERMISSIONS_CAMERA)) {
//            pickMediaActivity?.let { it.SetToSharePreference(this, PickMediaActivity.cameraNeverAskAgain, false) }
//        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pickMediaActivity?.let {
            data?.let { d ->
                it.activityResult(requireActivity(), PickMediaActivity.my_profile, requestCode, resultCode, d, false)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        try {
            pickMediaActivity?.let {
                it.activityPermissionsResult(requireContext(), requestCode, permissions, grantResults)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        var PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

}