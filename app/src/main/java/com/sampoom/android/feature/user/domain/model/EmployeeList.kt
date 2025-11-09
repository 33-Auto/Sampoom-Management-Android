package com.sampoom.android.feature.user.domain.model

data class EmployeeList(
    val items: List<Employee>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = EmployeeList(emptyList())
    }
}
