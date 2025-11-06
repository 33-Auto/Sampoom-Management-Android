package com.sampoom.android.feature.part.data.remote.dto

data class PartDto(
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long,
    val standardCost: Long
)
