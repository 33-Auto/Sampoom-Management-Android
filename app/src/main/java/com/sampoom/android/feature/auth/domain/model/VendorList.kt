package com.sampoom.android.feature.auth.domain.model

data class VendorList(
    val items: List<Vendor>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = VendorList(emptyList())
    }
}
