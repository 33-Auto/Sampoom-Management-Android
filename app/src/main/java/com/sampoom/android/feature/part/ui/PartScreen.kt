package com.sampoom.android.feature.part.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.White
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.Group

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartScreen(
    onNavigateBack: () -> Unit = {},
    onNavigatePartList: (Group) -> Unit,
    viewModel: PartViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.part_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // 검색 바
            OutlinedTextField(
                value = "uiState.searchQuery",
                onValueChange = { "viewModel.onSearchQueryChanged(it)" },
                placeholder = { Text(stringResource(R.string.part_placeholder_search)) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(R.string.part_title_search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                )
            )

            // Category 선택 제목
            Text(
                text = stringResource(R.string.part_title_category),
                style = MaterialTheme.typography.titleMedium,
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
                        modifier = Modifier.height(200.dp).fillMaxWidth()
                    )
                }

                uiState.categoryList.isEmpty() -> {
                    EmptyContent(
                        message = stringResource(R.string.part_empty_category),
                        modifier = Modifier.height(200.dp).fillMaxWidth()
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // 그룹 리스트
                when {
                    uiState.groupLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.groupError != null -> {
                        ErrorContent(
                            onRetry = { viewModel.onEvent(PartUiEvent.RetryGroups) },
                            modifier = Modifier.height(200.dp)
                        )
                    }

                    uiState.groupList.isEmpty() -> {
                        EmptyContent(
                            message = stringResource(R.string.part_empty_group),
                            modifier = Modifier.height(200.dp)
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
                                    onClick = { onNavigatePartList(group) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
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
                style = MaterialTheme.typography.titleMedium
            )

            Icon(
                painterResource(R.drawable.chevron_right),
                contentDescription = stringResource(R.string.common_detail)
            )
        }
    }
}