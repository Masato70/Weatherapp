package com.example.weatherapp


import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
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
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOError
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationDialog()

    }


    private fun locationDialog() {
        //パーミッションの状態確認

        //位置情報許可されている
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "位置情報許可されています")
            weatherTask()

        //位置情報許可されていない
        } else {
            Log.d(TAG, "位置情報許可されていないのでバーミッションダイアログを表示しました。")
            //パーミッションダイアログ表示
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }
    }

    private fun weatherTask() {

        lifecycleScope.launch {
            val result =weatherBackgroundTask()
            weatherJsonTask(result.toString())
        }
    }

    private suspend fun weatherBackgroundTask(){
        //緯度経度取得
        val response = withContext(Dispatchers.IO) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return@withContext
            }


            fusedLocationClient.lastLocation.addOnSuccessListener {
                val latitude :String = it.latitude.toString()
                val longitude :String = it.longitude.toString()
                val apiURL = "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&exclude=alerts,daily&appid="
                var http = ""

                try {
                    val urlObj = URL(apiURL)
                    val br = BufferedReader(InputStreamReader(urlObj.openStream()))
                    http = br.readText()

                }catch (e:IOException){e.printStackTrace()
                }catch (e:JSONException){e.printStackTrace()}
            }
            return@withContext
        }
        return response
    }

    private fun weatherJsonTask(reslt: String) {

        //jsonを取得
        val jsonObj = JSONObject(reslt)


    }

}