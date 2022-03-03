package com.example.home26

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.security.MessageDigest

class MyViewModel() : ViewModel() {

    private val responce: MutableLiveData<String> = MutableLiveData()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentCall:Call? = null
    private val rep = Repository()
    val audioLiveData = MutableLiveData<List<AudioEnity>>()

    private fun setResponse(value: String) {
        responce.postValue(value)
    }

    fun getResponse(): LiveData<String> {
        return responce
    }


    fun load() {
       scope.launch {
           val audios = withContext(Dispatchers.IO){
            rep.getTrack()
           }
           audioLiveData.value = audios
       }
    }

    fun sendRequest(login: String, password: String) {
        currentCall?.cancel()
        val apiSignature =
            "api_key" + ApiConst.APIKEY + "methodauth.getMobileSession" + "password" + password + "username" + login + ApiConst.APISECRETKEY
            val hexString = StringBuilder()

            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance("MD5")
            digest.update(apiSignature.toByteArray(charset("UTF-8")))
            val messageDigest = digest.digest()

            // Create Hex String
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            val urlParameter =
                "method=auth.getMobileSession&api_key=${ApiConst.APIKEY}&password=$password&username=$login&api_sig=$hexString"
            val request = Request.Builder()
                .url("https://ws.audioscrobbler.com/2.0/?$urlParameter")
                .post(RequestBody.create(null, ByteArray(0))).build()
            val client = OkHttpClient.Builder().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    response.body().use { responseBody ->
                        val test = responseBody!!.string()
                        setResponse(test)

                        Log.d("MyLog", test)
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    }
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        currentCall?.cancel()
    }
}