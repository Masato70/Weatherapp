package com.example.weatherapp


import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationinformation()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                tastBackground()
            }
        }
    }

    override fun onRequestPermissionsResult(


        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "位置情報にアクセスを許可されました", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "設定から位置情報にアクセスを許可してください", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun locationinformation() {
        //パーミッションの状態確認

        //位置情報許可されている
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "位置情報許可されています")
        //位置情報許可されていない
        } else {
            Log.d(TAG, "位置情報許可されていないのでバーミッションダイアログを表示しました。")
            //パーミッションダイアログ表示
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }
    }

    private fun tastBackground() {
        //緯度経度取

        Log.d(TAG , "通過")

    }
}