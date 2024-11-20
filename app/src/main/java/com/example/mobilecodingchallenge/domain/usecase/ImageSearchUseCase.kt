package com.example.mobilecodingchallenge.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mobilecodingchallenge.domain.model.ImageSearchSort
import com.example.mobilecodingchallenge.domain.model.ImageSearchWindow
import com.example.mobilecodingchallenge.domain.model.Item
import com.example.mobilecodingchallenge.domain.repository.interfaces.IImageSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageSearchUseCase @Inject constructor(
    private val imageSearchRepository: IImageSearchRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: ImageSearchSort = ImageSearchSort.TIME,
        window: ImageSearchWindow = ImageSearchWindow.ALL
    ): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                object : PagingSource<Int, Item>() {
                    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
                        return state.anchorPosition?.let { anchorPosition ->
                            val anchorPage = state.closestPageToPosition(anchorPosition)
                            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                        }
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
                        return try {
                            val currentPage = params.key ?: 0
                            val response = imageSearchRepository.getItemsBySearchQuery(
                                query,
                                sort,
                                window,
                                currentPage
                            )
                            LoadResult.Page(
                                data = response,
                                prevKey = if (currentPage == 0) null else currentPage - 1,
                                nextKey = if (response.isEmpty()) null else currentPage + 1
                            )
                        } catch (e: Exception) {
                            LoadResult.Error(e)
                        }
                    }
                }
            }
        ).flow
    }
}