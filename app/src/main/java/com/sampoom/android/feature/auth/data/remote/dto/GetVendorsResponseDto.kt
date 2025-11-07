package com.sampoom.android.feature.auth.data.remote.dto

data class GetVendorsResponseDto(
    val id: Long,
    val vendorCode: String,
    val name: String,
    val businessNumber: String,
    val ceoName: String,
    val address: String,
    val status: String
)
