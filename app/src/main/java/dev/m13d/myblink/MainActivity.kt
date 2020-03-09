package dev.m13d.myblink

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var CAMERA_PERMISSION = 200
    private var flashLightStatus: Boolean = false
    private var btAction: ImageButton? = null
    private var tvStatus: TextView? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btAction = findViewById(R.id.btAction)

        tvStatus!!.text = "OFF"
        btAction!!.setOnClickListener {
            val permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (permissions != PackageManager.PERMISSION_GRANTED) {
                    setupPermissions()
                } else {
                    openFlashLight()
                }
            } else {
                openFlashLight()
            }
        }
    }

    private fun setupPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.isEmpty() || !grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                } else {
                    openFlashLight()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openFlashLight() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (!flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                btAction!!.setImageDrawable(getDrawable(R.drawable.on_icon))
                tvStatus!!.text = "ON"
                flashLightStatus = true
            } catch (e: CameraAccessException) {

            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false)
                btAction!!.setImageDrawable(getDrawable(R.drawable.off_icon))
                tvStatus!!.text = "OFF"
                flashLightStatus = false
            } catch (e: CameraAccessException) {

            }
        }
    }
}
