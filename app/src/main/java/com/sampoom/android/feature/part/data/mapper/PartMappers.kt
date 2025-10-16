package com.sampoom.android.feature.part.data.mapper

import com.sampoom.android.feature.part.data.remote.dto.CategoryDto
import com.sampoom.android.feature.part.data.remote.dto.GroupDto
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.Group

fun CategoryDto.toModel(): Category = Category(id, code, name)
fun GroupDto.toModel(): Group = Group(id, code, name, categoryId)
