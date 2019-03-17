package com.existing.boilerx.ktx

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build


/**
 * Created by「 The Khaeng 」on 21 Mar 2018 :)
 */
fun Context.isCameraFrontAvailable(): Boolean {
    return this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
}

fun Context.isCameraHardwareAvailable(): Boolean {
    var hasCamera = false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val manager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            hasCamera = manager.cameraIdList.isNotEmpty()
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    } else {
        val numCameras = Camera.getNumberOfCameras()
        hasCamera = numCameras > 0
    }
    return this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) && hasCamera
}

fun Context.isFlashAvailable(): Boolean {
    return this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
}



