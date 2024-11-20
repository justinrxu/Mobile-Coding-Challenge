package com.example.mobilecodingchallenge.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Media {
    abstract val title: String?
    abstract val description: String?
    abstract val link: String
}