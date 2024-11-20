package com.example.mobilecodingchallenge.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.size.Size
import com.example.mobilecodingchallenge.domain.model.AnimatedGIFMedia
import com.example.mobilecodingchallenge.domain.model.AnimatedMP4Media
import com.example.mobilecodingchallenge.domain.model.Media
import com.example.mobilecodingchallenge.domain.model.StaticMedia

@Composable
fun MediaCard(
    media: Media,
    modifier: Modifier = Modifier,
    fill: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        when (media) {
            is AnimatedGIFMedia -> {
                val model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(media.link)
                    .size(Size.ORIGINAL)
                    .build()

                AsyncImage(
                    model = model,
                    contentDescription = null,
                    contentScale =
                        if (fill) ContentScale.FillBounds
                        else ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is AnimatedMP4Media -> {
                val context = LocalContext.current

                val mediaItem = MediaItem.Builder()
                    .setUri(media.link)
                    .build()
                val exoPlayer = remember(context, mediaItem) {
                    ExoPlayer.Builder(context)
                        .build()
                        .also { exoPlayer ->
                            exoPlayer.setMediaItem(mediaItem)
                            exoPlayer.prepare()
                            exoPlayer.playWhenReady = true
                            exoPlayer.repeatMode = REPEAT_MODE_ALL
                        }
                }
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                DisposableEffect(Unit) { onDispose { exoPlayer.release() } }
            }
            is StaticMedia -> {
                val model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(media.link)
                    .size(Size.ORIGINAL)
                    .build()

                AsyncImage(
                    model = model,
                    contentScale =
                        if (fill) ContentScale.FillBounds
                        else ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}