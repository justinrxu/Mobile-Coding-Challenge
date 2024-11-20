package com.example.mobilecodingchallenge.domain.model

import kotlinx.serialization.Serializable

@Serializable
class AnimatedMP4Media(
    override val title: String?,
    override val description: String?,
    override val link: String
) : Media()