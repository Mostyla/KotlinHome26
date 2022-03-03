package com.example.home26

import com.google.gson.annotations.SerializedName

data class AudioDTO(
    @SerializedName("tracks")
    val tracks: Tracks? = null
)

data class Tracks(
    @SerializedName("track")
    val track: List<Track>? = null
)


data class Track(
    @SerializedName("artist")
    val artist: Artist? = null,
    @SerializedName("image")
    val image: List<Image>? = null,
    @SerializedName("name")
    val name: String? = null
)

data class Artist(
    @SerializedName("name")
    val name: String? = null,
)

data class Image(
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("#text")
    val text: String? = null
)
