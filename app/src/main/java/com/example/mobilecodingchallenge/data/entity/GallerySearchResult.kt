package com.example.mobilecodingchallenge.data.entity

import com.google.gson.annotations.SerializedName

data class GallerySearchResult(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("animated")
    val animated: Boolean?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("datetime")
    val datetime: Long,

    @SerializedName("type")
    val type: String,

    @SerializedName("views")
    val views: Int,

    @SerializedName("ups")
    val ups: Int,

    @SerializedName("downs")
    val downs: Int,

    @SerializedName("points")
    val points: Int,

    @SerializedName("nsfw")
    val isNsfw: Boolean,

    @SerializedName("is_ad")
    val isAd: Boolean?,

    @SerializedName("is_album")
    val isAlbum: Boolean?,

    @SerializedName("images")
    val images: List<GalleryMedia>?,

    @SerializedName("link")
    val link: String?,

    @SerializedName("mp4")
    val mp4: String?,
)