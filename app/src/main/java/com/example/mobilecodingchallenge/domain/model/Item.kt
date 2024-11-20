package com.example.mobilecodingchallenge.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeComponentSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val title: String?,
    val description: String?,
    @Serializable(with = LocalDateTimeComponentSerializer::class)
    val datetime: LocalDateTime,
    val views: Int,
    val media: List<Media>
) {
    val coverMedia: Media
        get() = media.first()
}