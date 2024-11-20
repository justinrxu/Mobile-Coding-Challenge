package com.example.mobilecodingchallenge.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mobilecodingchallenge.domain.model.ImageSearchSort
import com.example.mobilecodingchallenge.domain.model.ImageSearchWindow
import com.example.mobilecodingchallenge.domain.model.Item
import com.example.mobilecodingchallenge.ui.MediaCard
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(mainViewModel: MainViewModel, navController: NavController) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember { mutableStateOf(false) }

    val uiState: MainViewModel.MainUIState by
        mainViewModel.mainUIState.collectAsStateWithLifecycle()

    var query: String by rememberSaveable { mutableStateOf("") }
    var sort: ImageSearchSort by rememberSaveable { mutableStateOf(ImageSearchSort.TIME) }
    var window: ImageSearchWindow by rememberSaveable { mutableStateOf(ImageSearchWindow.ALL) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) { padding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                var selectedSort: ImageSearchSort by remember { mutableStateOf(sort) }
                var selectedWindow: ImageSearchWindow by remember { mutableStateOf(window) }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "By Sort",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    item {
                        FilterOptions(
                            options = ImageSearchSort.entries,
                            selectedOption = selectedSort,
                            onOptionSelected = { selectedSort = it }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    item {
                        Text(
                            text = "By Window",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        FilterOptions(
                            options = ImageSearchWindow.entries,
                            selectedOption = selectedWindow,
                            onOptionSelected = { selectedWindow = it }
                        )
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    selectedSort = sort
                                    selectedWindow = window
                                }
                            ) {
                                Text(text = "Reset")
                            }
                            Button(
                                onClick = {
                                    sort = selectedSort
                                    window = selectedWindow
                                    if (query.isNotBlank()) {
                                        mainViewModel.searchImagesByQuery(query, sort, window)
                                    }
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }
                            ) {
                                Text(text = "Apply")
                            }
                        }
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text(text = "Search") },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .weight(1f)
                )
                IconButton(
                    onClick = {
                        if (query.isNotBlank()) {
                            mainViewModel.searchImagesByQuery(query, sort, window)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = null)
                }
            }
            uiState.let { uiState ->
                when(uiState) {
                    MainViewModel.MainUIState.Empty -> { }
                    is MainViewModel.MainUIState.Loaded -> {
                        val pagingItems: LazyPagingItems<Item> =
                            uiState.pagingItems.collectAsLazyPagingItems()

                        LoadedResultsList(
                            items = pagingItems,
                            itemOnClick = { item ->
                                mainViewModel.selectedItem = item
                                navController.navigate("/mediaGalleryView")
                            }
                        )
                    }
                    MainViewModel.MainUIState.Loading -> {
                        CircularProgressIndicator(color = Color.Blue)
                    }

                    is MainViewModel.MainUIState.Error -> {
                        Text(text = "Error fetching results, please try again later.")
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadedResultsList(
    items: LazyPagingItems<Item>,
    itemOnClick: (Item) -> Unit
) {
    val cardSize = 250.dp

    LazyVerticalGrid(
        columns = GridCells.Adaptive(cardSize),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(items.itemCount) { i ->
            val item = items[i]!!
            SearchItem(
                item = item,
                itemOnClick = { itemOnClick(item) },
                size = cardSize
            )
        }
        with(items.loadState) {
            when {
                refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.size(80.dp)) }
                }

                refresh is LoadState.Error -> {
                    val error = refresh as LoadState.Error
                    item {
                        Text(
                            text = error.error.localizedMessage!!,
                            color = Color.Red,
                        )
                    }
                }

                append is LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.size(80.dp)) }
                }

                append is LoadState.Error -> {
                    val error = append as LoadState.Error
                    item {
                        Text(
                            text = error.error.localizedMessage!!,
                            color = Color.Red,
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun SearchItem(
    item: Item, 
    itemOnClick: () -> Unit,
    size: Dp = 250.dp
) {
    Surface(
        onClick = itemOnClick,
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(vertical = 10.dp)
        ) {
            MediaCard(
                media = item.coverMedia,
                fill = true,
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
            Spacer(modifier = Modifier.height(4.dp))
            item.title?.let { Text(text = it, overflow = TextOverflow.Ellipsis, maxLines = 1) }
        }
    }
}

@Composable
fun <T: Enum<*>> FilterOptions(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onOptionSelected(option) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option.name.lowercase().replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString()
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
