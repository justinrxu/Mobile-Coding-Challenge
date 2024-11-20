package com.example.mobilecodingchallenge.data.entity

import com.google.gson.annotations.SerializedName

data class GalleryMedia(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("datetime")
    val datetime: Long,

    @SerializedName("type")
    val type: String,

    @SerializedName("animated")
    val animated: Boolean?,

    @SerializedName("views")
    val views: Int,

    @SerializedName("nsfw")
    val isNsfw: Boolean?,

    @SerializedName("link")
    val link: String?,

    @SerializedName("mp4")
    val mp4: String?,

    @SerializedName("gifv")
    val gifv: String?,

    @SerializedName("ups")
    val ups: Int?,

    @SerializedName("downs")
    val downs: Int?,

    @SerializedName("points")
    val points: Int?,

    @SerializedName("score")
    val score: Int?
)