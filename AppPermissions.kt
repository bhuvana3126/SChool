package com.wholesale.jewels.fauxiq.baheekhata.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AppPermissions {

    private var permissionGrandListener: PermissionGrandListener? = null

    fun checkPermission(
        activity: Activity,
        permission: Int,
        permissionGrandListener: PermissionGrandListener
    ) {
        this.permissionGrandListener = permissionGrandListener

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    activity,

                    when (permission) {
                        PERMISSION_CAMERA -> Manifest.permission.CAMERA
                        PERMISSION_EXTERNAL_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
                        PERMISSION_EXTERNAL_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
                        else -> Manifest.permission.CAMERA
                    }

                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    when (permission) {
                        PERMISSION_CAMERA -> arrayOf(Manifest.permission.CAMERA)
                        PERMISSION_EXTERNAL_STORAGE -> arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        else -> arrayOf(Manifest.permission.CAMERA)
                    }
                    ,
                    permission
                )

            } else {
                permissionGrandListener.onGrand()
            }

        } else {
            permissionGrandListener.onGrand()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (permissionGrandListener != null) {
                permissionGrandListener!!.onGrand()
            }
        } else {

            if (permissionGrandListener != null) {
                permissionGrandListener!!.onDenied()
            }
        }
    }

    interface PermissionGrandListener {
        fun onGrand()
        fun onDenied()
    }

    companion object {

        private var INSTANCE: AppPermissions? = null

        fun instance(): AppPermissions {

            if (INSTANCE == null) {
                INSTANCE = AppPermissions()
            }
            return INSTANCE!!
        }

        const val PERMISSION_CAMERA = 101
        const val PERMISSION_EXTERNAL_STORAGE: Int = 102
    }

}