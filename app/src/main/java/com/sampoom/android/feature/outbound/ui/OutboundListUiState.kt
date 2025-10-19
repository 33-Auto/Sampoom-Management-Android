package com.sampoom.android.feature.outbound.ui

import com.sampoom.android.feature.outbound.domain.model.Outbound

data class OutboundListUiState(
    val outboundList: List<Outbound> = emptyList(),
    val outboundLoading: Boolean = false,
    val outboundError: String? = null,
    val selectedOutbound: Outbound? = null,
    val isUpdating: Boolean = false,
    val updateError: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null
)