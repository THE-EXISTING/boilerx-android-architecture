package com.existing.boilerx.ktx


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.storage.StorageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import java.lang.reflect.InvocationTargetException
import java.net.NetworkInterface
import java.util.*


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */

val Context.isTablet: Boolean
    get() = this.resources.getBoolean(R.bool.is_tablet)

val Context.deviceId: String
    get() = this.androidID + ":" + Build.SERIAL + ":" + this.macAddress

val Context.androidID: String
    @SuppressLint("HardwareIds")
    get() = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

val deviceName: String get() = Build.MODEL

val Context.macAddress: String
    get() {
        var macAddress = getMacAddressByWifiInfo()
        if ("02:00:00:00:00:00" != macAddress) return macAddress

        macAddress = getMacAddressByNetworkInterface()
        if ("02:00:00:00:00:00" != macAddress) return macAddress

        return "02:00:00:00:00:00"
    }

val Context.macAddressWithUUID: String
    get() {
        if(BuildConfig.DEBUG) {
            //random UUID only debug
            val prefs = getSharedPreferences("debug_pref", Context.MODE_PRIVATE)
            var uuid = prefs.getString("uuid", null)
            if (uuid == null) {
                uuid = UUID.randomUUID().toString().toUUID()
                val editor = prefs.edit()
                editor.putString("uuid", uuid)
                editor.apply()
            }
            return uuid
        }


        var macAddress = this.macAddress
        if ("02:00:00:00:00:00" != macAddress) return macAddress

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                macAddress = Build.getSerial().toUUID()
            }
        }

        return macAddress
    }


val Context.isSDCardEnable: Boolean get() = this.SDCardPaths.isNotEmpty()

val Context.SDCardPaths: List<String>
    get() {
        val storageManager = this.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        var paths: List<String> = ArrayList()
        try {
            val getVolumePathsMethod = StorageManager::class.java.getMethod("getVolumePaths")
            getVolumePathsMethod.isAccessible = true
            val invoke = getVolumePathsMethod.invoke(storageManager)
            paths = Arrays.asList(invoke as String)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return paths
    }

/* =========================== Private method =================================================== */

/** <p>{@code <uses-permission android:name="android.permission.INTERNET"/>}</p> **/
@SuppressLint("HardwareIds")
private fun Context.getMacAddressByWifiInfo(): String {
    return try {
        val wifi = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.connectionInfo
        info?.macAddress ?: "02:00:00:00:00:00"
    } catch (e: Exception) {
        e.printStackTrace()
        "02:00:00:00:00:00"
    }
}

private fun getMacAddressByNetworkInterface(): String {
    try {
        val nis = Collections.list<NetworkInterface>(NetworkInterface.getNetworkInterfaces())
        for (ni in nis) {
            if (ni.name.equals("wlan0", ignoreCase = true).not()) continue
            val macBytes = ni.hardwareAddress
            if (macBytes?.isNotEmpty() == true) {
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02x:", b))
                }
                return res1.deleteCharAt(res1.length - 1).toString()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return "02:00:00:00:00:00"
}

