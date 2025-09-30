package com.sampoom.android.feature.part.data.mapper

import com.sampoom.android.feature.part.data.remote.dto.PartDto
import com.sampoom.android.feature.part.domain.model.Part

fun PartDto.toModel(): Part = Part(id, name, count)