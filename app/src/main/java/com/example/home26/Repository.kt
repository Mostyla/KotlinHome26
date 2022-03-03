package com.example.home26

import com.example.home26.ApiConst.APIKEY
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class Repository () {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val gson = Gson()

    fun getTrack(): List<AudioEnity> {

        val urlAdressGetAudio =
            "https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=${APIKEY}&format=json"
        val responseAudio =
            okHttpClient
                .newCall(
                    Request.Builder()
                        .url(urlAdressGetAudio)
                        .build()
                ).execute()
        val jsonString = responseAudio.body()?.string().orEmpty()
        val json = gson.fromJson(jsonString, AudioDTO::class.java)

        val audioEntity = json.tracks?.track?.map {

            AudioEnity(
                artistName = it.artist?.name.orEmpty(),
                image = it.image?.get(1)?.text.orEmpty(),
                audioName = it.name.orEmpty(),
            )
        }.orEmpty()

        return audioEntity
    }
}
