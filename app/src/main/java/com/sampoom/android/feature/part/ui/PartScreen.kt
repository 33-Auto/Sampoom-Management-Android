package com.sampoom.android.feature.part.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.White
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.backgroundColor
import com.sampoom.android.core.ui.theme.disableColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.Group
import com.sampoom.android.feature.part.domain.model.Part
import com.sampoom.android.feature.part.domain.model.SearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartScreen(
    onNavigateBack: () -> Unit = {},
    onNavigatePartList: (Group) -> Unit,
    viewModel: PartViewModel = hiltViewModel(),
    searchViewModel: PartListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val searchResultsPaged = viewModel.searchResult.collectAsLazyPagingItems()
    var textFieldState by remember { mutableStateOf(TextFieldValue("")) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    // 자동 검색을 위한 LaunchedEffect
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // ModalBottomSheet 상태 관리
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text,
                    onQueryChange = { newText ->
                        textFieldState = textFieldState.copy(newText)

                        // 기존 검색 작업 취소
                        searchJob?.cancel()

                        if (newText.isNotBlank()) {
                            // 500ms 지연 후 검색 실행
                            searchJob = coroutineScope.launch {
                                delay(500)
                                if (newText.isNotBlank()) {
                                    viewModel.onEvent(PartUiEvent.Search(newText))
                                }
                            }
                        } else {
                            // 검색어가 비어있으면 검색 결과 초기화
                            viewModel.onEvent(PartUiEvent.Search(""))
                        }
                    },
                    onSearch = {
                        viewModel.onEvent(PartUiEvent.Search(textFieldState.text))
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(R.string.part_title_search)) },
                    leadingIcon = {
                        if (expanded) {
                            IconButton(onClick = { expanded = false }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_back),
                                    contentDescription = stringResource(R.string.nav_back)
                                )
                            }
                        } else {
                            IconButton(onClick = { onNavigateBack() }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_back),
                                    contentDescription = stringResource(R.string.nav_back)
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        if (textFieldState.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    textFieldState = textFieldState.copy("")
                                    viewModel.onEvent(PartUiEvent.Search(""))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "검색어 지우기"
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundCardColor(),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = textSecondaryColor().copy(0.3f),
                        focusedTrailingIconColor = textSecondaryColor(),
                        unfocusedTrailingIconColor = textSecondaryColor()
                    )
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            colors = SearchBarDefaults.colors(
                containerColor = backgroundColor(),
                dividerColor = Color.Transparent
            )
        ) {
            when {
                textFieldState.text.isBlank() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.part_search_description),
                            color = textSecondaryColor(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                searchResultsPaged.loadState.refresh is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                searchResultsPaged.loadState.refresh is LoadState.Error -> {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(PartUiEvent.Search(textFieldState.text)) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                searchResultsPaged.itemCount == 0 && textFieldState.text.isNotBlank() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyContent(
                            message = stringResource(R.string.part_search_empty)
                        )
                    }
                }

                else -> {
                    SearchResultsList(
                        searchResults = searchResultsPaged,
                        onPartClick = { part ->
                            searchViewModel.onEvent(PartListUiEvent.ShowBottomSheet(part))
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(100.dp))

            // Category 선택 제목
            Text(
                text = stringResource(R.string.part_title_category),
                style = MaterialTheme.typography.titleMedium,
                color = textColor(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Category 섹션
            when {
                uiState.categoryLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.categoryError != null -> {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(PartUiEvent.RetryCategories) },
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }

                uiState.categoryList.isEmpty() -> {
                    EmptyContent(
                        message = stringResource(R.string.part_empty_category),
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }

                else -> {
                    // 2x3 그리드로 카테고리 배치
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.categoryList.chunked(3).forEach { categoryChunk ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                categoryChunk.forEach { category ->
                                    CategoryItem(
                                        category = category,
                                        isSelected = category.id == uiState.selectedCategory?.id,
                                        onClick = {
                                            viewModel.onEvent(
                                                PartUiEvent.CategorySelected(
                                                    category
                                                )
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                // 3개 미만인 경우 빈 공간 채우기
                                repeat(3 - categoryChunk.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 그룹 리스트 섹션
            if (uiState.selectedCategory == null) {
                // 초기 상태: 카테고리 선택 안내
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = textSecondaryColor()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.part_select_category),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondaryColor()
                    )
                }
            } else {
                // 그룹 선택 제목
                Text(
                    text = stringResource(R.string.part_title_group),
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // 그룹 리스트
                when {
                    uiState.groupLoading -> {
                        Box(
                            modifier = Modifier.height(200.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.groupError != null -> {
                        ErrorContent(
                            onRetry = { viewModel.onEvent(PartUiEvent.RetryGroups) },
                            modifier = Modifier.height(200.dp).fillMaxWidth()
                        )
                    }

                    uiState.groupList.isEmpty() -> {
                        EmptyContent(
                            message = stringResource(R.string.part_empty_group),
                            modifier = Modifier.height(200.dp).fillMaxWidth()
                        )
                    }

                    else -> {
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.groupList.forEach { group ->
                                PartItemCard(
                                    group = group,
                                    onClick = {
                                        onNavigatePartList(group)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }

    if (showBottomSheet) {
        searchUiState.selectedPart?.let { selectedPart ->
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    searchViewModel.onEvent(PartListUiEvent.DismissBottomSheet)
                },
                sheetState = sheetState
            ) {
                PartDetailBottomSheet(
                    part = selectedPart,
                    onDismiss = {
                        showBottomSheet = false
                        searchViewModel.onEvent(PartListUiEvent.DismissBottomSheet)
                    }
                )
            }
        }
    }
}

// Category 아이템
@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Main500 else backgroundCardColor()
        ),
        modifier = modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = resourceMapper(category.code)),
                contentDescription = category.name,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) White else textColor()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) White else textColor()
            )
        }
    }
}

private fun resourceMapper(code: String): Int {
    return when (code) {
        "ENG" -> R.drawable.engine
        "TRN" -> R.drawable.transmission
        "CHS" -> R.drawable.chassis
        "BDY" -> R.drawable.body
        "TRM" -> R.drawable.trim
        "ELE" -> R.drawable.electric
        else -> R.drawable.parts
    }
}

@Composable
private fun PartItemCard(
    group: Group,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.titleMedium,
                color = textColor()
            )

            Icon(
                painterResource(R.drawable.chevron_right),
                contentDescription = stringResource(R.string.common_detail),
                tint = disableColor()
            )
        }
    }
}

@Composable
fun SearchResultsList(
    searchResults: LazyPagingItems<SearchResult>,
    onPartClick: (Part) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(
            count = searchResults.itemCount,
            key = searchResults.itemKey { it.part.partId }
        ) { index ->
            val partWithContext = searchResults[index]
            if (partWithContext != null) {
                SearchPartItem(
                    part = partWithContext.part,
                    categoryName = partWithContext.categoryName,
                    groupName = partWithContext.groupName,
                    onClick = { onPartClick(partWithContext.part) }
                )
            }
        }
    }
}

@Composable
private fun SearchPartItem(
    part: Part,
    categoryName: String,
    groupName: String,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundCardColor()
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$categoryName > $groupName",
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondaryColor()
                )

                Text(
                    text = part.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor()
                )

                Text(
                    text = part.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondaryColor(),
                    fontWeight = FontWeight.Light
                )
            }

            Text(
                text = "${part.quantity}",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor()
            )

            Icon(
                painterResource(R.drawable.chevron_right),
                contentDescription = stringResource(R.string.common_detail),
                tint = disableColor()
            )
        }
    }
}