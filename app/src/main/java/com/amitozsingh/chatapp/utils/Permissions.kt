package com.amitozsingh.chatapp.utils

import android.Manifest
import android.Manifest.permission
import androidx.core.app.ActivityCompat
import android.widget.Toast
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.amitozsingh.chatapp.Activities.MessagesActivity


class Permissions(private val mActivity: MessagesActivity) {

    fun checkPermissionForReadExternalStorage(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(mActivity, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForWriteExternalStorage(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(mActivity, permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }


    fun requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                mActivity,
                READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                mActivity,
                " External Storage permission is needed. Please turn it on inside the settings",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(READ_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE
            )
        }
    }


    fun requestPermissionForWriteExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                mActivity,
                " Write Storage permission is needed. Please turn it on inside the settings",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                mActivity,
                permission.CAMERA
            )
        ) {
            Toast.makeText(
                mActivity,
                " Camera permission is needed. Please turn it on inside the settings",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {

        private val EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE = 10

        private val EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE = 11

        private val CAMERA_PERMISSION_REQUEST_CODE = 12
    }
}
