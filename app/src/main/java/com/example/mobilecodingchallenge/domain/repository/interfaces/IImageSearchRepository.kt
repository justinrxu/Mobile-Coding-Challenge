package com.example.mobilecodingchallenge.domain.repository.interfaces

import com.example.mobilecodingchallenge.domain.model.ImageSearchSort
import com.example.mobilecodingchallenge.domain.model.ImageSearchWindow
import com.example.mobilecodingchallenge.domain.model.Item

interface IImageSearchRepository {
    suspend fun getItemsBySearchQuery(
        query: String,
        sort: ImageSearchSort,
        window: ImageSearchWindow,
        page: Int = 0
    ): List<Item>
}