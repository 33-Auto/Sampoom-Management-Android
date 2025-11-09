package com.sampoom.android.feature.user.domain.usecase

import androidx.paging.PagingData
import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeeUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<PagingData<Employee>> = repository.getEmployeeList()
}