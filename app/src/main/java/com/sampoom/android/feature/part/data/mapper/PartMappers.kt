package com.sampoom.android.feature.part.data.mapper

import com.sampoom.android.feature.part.data.remote.dto.CategoryDto
import com.sampoom.android.feature.part.data.remote.dto.GroupDto
import com.sampoom.android.feature.part.data.remote.dto.PartDto
import com.sampoom.android.feature.part.data.remote.dto.SearchCategoryDto
import com.sampoom.android.feature.part.data.remote.dto.SearchGroupDto
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.Group
import com.sampoom.android.feature.part.domain.model.Part
import com.sampoom.android.feature.part.domain.model.SearchCategory
import com.sampoom.android.feature.part.domain.model.SearchGroup

fun CategoryDto.toModel(): Category = Category(id, code, name)
fun GroupDto.toModel(): Group = Group(id, code, name, categoryId)
fun PartDto.toModel(): Part = Part(partId, code, name, quantity)

fun SearchCategoryDto.toModel(): SearchCategory = SearchCategory(categoryId, categoryName, groups.map { it.toModel() })
fun SearchGroupDto.toModel(): SearchGroup = SearchGroup(groupId, groupName, parts.map { it.toModel() })