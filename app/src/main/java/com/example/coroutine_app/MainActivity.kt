package com.example.coroutine_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var adviceText: TextView
    private lateinit var adviceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adviceText = findViewById(R.id.tvAdvice)
        adviceButton = findViewById(R.id.btnAdvice)

        adviceButton.setOnClickListener{displayData()}

    }

    private fun displayData() {
        CoroutineScope(IO).launch {
            val data = async { fetchData() }.await()
            if (data.isNotEmpty()){
                giveAdvice(data)
            } else {
                Log.d("MAIN", "Unable to get data")
            }
        }
    }

    private fun fetchData(): String {
        var response = ""
        try {
            response = URL("https://api.adviceslip.com/advice").readText()
        } catch (e: Exception) {
            Log.d("MAIN", "ISSUE: $e")
        }
        return response
    }

    private suspend fun giveAdvice(data: String) {
        withContext(Main) {
            val jsonObject = JSONObject(data)
            val advice = jsonObject.getJSONObject("slip").getString("advice")
            adviceText.text = advice
        }
    }
}