package com.sampoom.android.feature.auth.domain.model

data class Vendor(
    val id: Long,
    val vendorCode: String,
    val name: String,
    val businessNumber: String,
    val ceoName: String,
    val address: String,
    val status: String
)
