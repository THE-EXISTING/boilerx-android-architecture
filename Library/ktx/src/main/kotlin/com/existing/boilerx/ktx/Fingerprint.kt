package com.existing.boilerx.ktx

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import androidx.annotation.RequiresApi


/**
 * Created by「 The Khaeng 」on 27 May 2018 :)
 */

// <uses-permission android:name="android.permission.USE_FINGERPRINT" />


val Context.fingerManager
    @RequiresApi(Build.VERSION_CODES.M)
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getSystemService<FingerprintManager>(FingerprintManager::class.java)
    } else {
        null
    }


fun Context.isFingerprintHardwareAvailable(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.fingerManager?.isHardwareDetected ?: false
        } else {
            false
        }

fun Context.hasEnrolledFingerprints(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.fingerManager?.hasEnrolledFingerprints() ?: false
        } else {
            false
        }


fun Context.authenticateFingerprint(cryptoObject: FingerprintManager.CryptoObject,
                                    cancellationSignal: CancellationSignal,
                                    flags: Int,
                                    callback: FingerprintManager.AuthenticationCallback,
                                    handler: Handler?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.fingerManager?.authenticate(cryptoObject, cancellationSignal, flags, callback, handler)
    }
}




