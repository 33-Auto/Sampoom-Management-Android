package com.sampoom.android.feature.user.domain.repository

import androidx.paging.PagingData
import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getStoredUser(): User?
    suspend fun getProfile(role: String): Result<User>
    suspend fun updateProfile(user: User): Result<User>
    fun getEmployeeList(): Flow<PagingData<Employee>>
    suspend fun editEmployee(employee: Employee, role: String): Result<Employee>
    suspend fun updateEmployeeStatus(employee: Employee, role: String): Result<Employee>
    suspend fun getEmployeeCount(): Result<Int>
}