package com.sampoom.android.feature.part.data.mapper

import com.sampoom.android.feature.part.data.remote.dto.CategoryDto
import com.sampoom.android.feature.part.data.remote.dto.GroupDto
import com.sampoom.android.feature.part.data.remote.dto.PartDto
import com.sampoom.android.feature.part.data.remote.dto.SearchCategoryDto
import com.sampoom.android.feature.part.data.remote.dto.SearchDataDto
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.Group
import com.sampoom.android.feature.part.domain.model.Part
import com.sampoom.android.feature.part.domain.model.SearchResult

fun CategoryDto.toModel(): Category = Category(id, code, name)
fun GroupDto.toModel(): Group = Group(id, code, name, categoryId)
fun PartDto.toModel(): Part = Part(partId, code, name, quantity, standardCost)

fun SearchCategoryDto.toModel(): List<SearchResult> {
    return groups.flatMap { group ->
        group.parts.map { part ->
            SearchResult(
                part = part.toModel(),
                categoryName = categoryName,
                groupName = group.groupName
            )
        }
    }
}

fun SearchDataDto.toModel(): List<SearchResult> {
    return content.flatMap { category ->
        category.toModel()
    }
}