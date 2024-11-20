package com.example.mobilecodingchallenge.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.mobilecodingchallenge.domain.model.ImageSearchSort
import com.example.mobilecodingchallenge.domain.model.ImageSearchWindow
import com.example.mobilecodingchallenge.domain.model.Item
import com.example.mobilecodingchallenge.domain.usecase.ImageSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val imageSearchUseCase: ImageSearchUseCase
) : ViewModel() {
    sealed class MainUIState {
        data object Loading : MainUIState()
        data class Loaded(val pagingItems: Flow<PagingData<Item>>): MainUIState()
        data object Empty : MainUIState()
        data class Error(val error: String?) : MainUIState()
    }

    private val _mainUIState: MutableStateFlow<MainUIState> =
        MutableStateFlow(MainUIState.Empty)
    val mainUIState: StateFlow<MainUIState> = _mainUIState

    var selectedItem: Item? = null

    fun searchImagesByQuery(
        query: String,
        sort: ImageSearchSort = ImageSearchSort.TIME,
        window: ImageSearchWindow = ImageSearchWindow.ALL
    ) {
        _mainUIState.value = MainUIState.Loading
        viewModelScope.launch {
            _mainUIState.value = try {
                MainUIState.Loaded(
                    imageSearchUseCase.invoke(query, sort, window)
                )
            } catch (e: Exception) {
                MainUIState.Error(e.message)
            }
        }
    }
}