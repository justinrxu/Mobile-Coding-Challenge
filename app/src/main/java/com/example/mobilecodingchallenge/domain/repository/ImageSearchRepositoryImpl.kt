package com.example.mobilecodingchallenge.domain.repository

import com.example.mobilecodingchallenge.data.api.ImgurService
import com.example.mobilecodingchallenge.domain.model.AnimatedGIFMedia
import com.example.mobilecodingchallenge.domain.model.AnimatedMP4Media
import com.example.mobilecodingchallenge.domain.model.ImageSearchSort
import com.example.mobilecodingchallenge.domain.model.ImageSearchWindow
import com.example.mobilecodingchallenge.domain.model.Item
import com.example.mobilecodingchallenge.domain.model.Media
import com.example.mobilecodingchallenge.domain.model.StaticMedia
import com.example.mobilecodingchallenge.domain.repository.interfaces.IImageSearchRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class ImageSearchRepositoryImpl @Inject constructor(
    private val imgurService: ImgurService
) : IImageSearchRepository {
    private companion object {
        private  const val GIF_TYPE = "image/gif"
        private const val MP4_TYPE = "video/mp4"
    }
    override suspend fun getItemsBySearchQuery(
        query: String,
        sort: ImageSearchSort,
        window: ImageSearchWindow,
        page: Int
    ): List<Item> {
        return imgurService.getGallerySearchResults(
            when(sort) {
                ImageSearchSort.TIME -> "time"
                ImageSearchSort.VIRAL -> "viral"
                ImageSearchSort.TOP -> "top"
            },
            when(window) {
                ImageSearchWindow.DAY -> "day"
                ImageSearchWindow.WEEK -> "week"
                ImageSearchWindow.MONTH -> "month"
                ImageSearchWindow.YEAR -> "year"
                ImageSearchWindow.ALL -> "all"
            },
            page,
            query
        ).data.mapNotNull { gallerySearchResults ->
            with(gallerySearchResults) {
                val media: List<Media>? =
                    if (gallerySearchResults.isAlbum == true && images != null) {
                        images.mapNotNull { galleryImage ->
                            with(galleryImage) {
                                if (galleryImage.animated == false && link != null) {
                                    StaticMedia(title, description, link)
                                } else if (galleryImage.type == MP4_TYPE && mp4 != null) {
                                    AnimatedMP4Media(title, description, mp4)
                                } else if (galleryImage.type == GIF_TYPE && link != null) {
                                    AnimatedGIFMedia(title, description, link)
                                } else {
                                    null
                                }
                            }
                        }
                    } else if (gallerySearchResults.animated == false && link != null) {
                        listOf(StaticMedia(title, description, link))
                    } else if (gallerySearchResults.type == MP4_TYPE && mp4 != null) {
                        listOf(AnimatedMP4Media(title, description, mp4))
                    } else if (gallerySearchResults.type == GIF_TYPE && link != null) {
                        listOf(AnimatedGIFMedia(title, description, link))
                    } else {
                        null
                    }

                if (media != null) {
                    Item(
                        id = id,
                        title = title,
                        description = description,
                        datetime = Instant
                            .fromEpochSeconds(datetime)
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                        views = views,
                        media = media
                    )
                } else {
                    null
                }
            }
        }
    }
}