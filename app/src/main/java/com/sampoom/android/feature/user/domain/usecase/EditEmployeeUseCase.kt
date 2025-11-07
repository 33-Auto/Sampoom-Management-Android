package com.sampoom.android.feature.user.domain.usecase

import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class EditEmployeeUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(employee: Employee, workspace: String): Result<Employee> = repository.editEmployee(employee, workspace)
}