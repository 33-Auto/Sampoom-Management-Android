package com.sampoom.android.feature.user.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.core.util.retry
import com.sampoom.android.feature.user.data.mapper.toModel
import com.sampoom.android.feature.user.data.paging.EmployeePagingSource
import com.sampoom.android.feature.user.data.remote.api.UserApi
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeRequestDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileRequestDto
import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val preferences: AuthPreferences,
    private val pagingSourceFactory: EmployeePagingSource.Factory
) : UserRepository {
    override suspend fun getStoredUser(): User? {
        return preferences.getStoredUser()
    }

    override suspend fun getProfile(workspace: String): Result<User> {
        return runCatching {
            retry(times = 5, initialDelay = 300) {
                val dto = api.getProfile(workspace)
                if (!dto.success) throw Exception(dto.message)
                val profileUser = dto.data.toModel()
                val loginUser = preferences.getStoredUser()

                val completeUser = if (loginUser != null) {
                    User(
                        userId = profileUser.userId,
                        userName = profileUser.userName,
                        email = profileUser.email,
                        role = profileUser.role,
                        accessToken = loginUser.accessToken,      // 저장된 토큰
                        refreshToken = loginUser.refreshToken,    // 저장된 토큰
                        expiresIn = loginUser.expiresIn,          // 저장된 토큰
                        position = profileUser.position,
                        workspace = profileUser.workspace,
                        branch = profileUser.branch,
                        agencyId = profileUser.agencyId,
                        startedAt = profileUser.startedAt,
                        endedAt = profileUser.endedAt
                    )
                } else {
                    throw Exception()
                }

                preferences.saveUser(completeUser)
                completeUser
            }
        }
    }

    override suspend fun updateProfile(user: User): Result<User> {
        return runCatching {
            val requestDto = UpdateProfileRequestDto(
                userName = user.userName
            )
            val dto = api.updateProfile(requestDto)
            if (!dto.success) throw Exception(dto.message)
            val updatedProfile = dto.data.toModel()
            val storedUser = preferences.getStoredUser()
            val completeUser = if (storedUser != null) {
                User(
                    userId = updatedProfile.userId,
                    userName = updatedProfile.userName,
                    email = user.email,
                    role = user.role,
                    accessToken = storedUser.accessToken,
                    refreshToken = storedUser.refreshToken,
                    expiresIn = storedUser.expiresIn,
                    position = user.position,
                    workspace = user.workspace,
                    branch = user.branch,
                    agencyId = user.agencyId,
                    startedAt = user.startedAt,
                    endedAt = user.endedAt
                )
            } else throw Exception()

            preferences.saveUser(completeUser)
            completeUser
        }
    }

    override fun getEmployeeList(): Flow<PagingData<Employee>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceFactory.create() }
        ).flow
    }

    override suspend fun editEmployee(
        employee: Employee,
        workspace: String
    ): Result<Employee> {
        return runCatching {
            val requestDto = EditEmployeeRequestDto(
                position = employee.position.name
            )
            val dto = api.editEmployee(
                userId = employee.userId,
                workspace = workspace,
                body = requestDto
            )
            if (!dto.success) throw Exception(dto.message)

            val updatedEmployee = dto.data.toModel()
            val completeEmployee = Employee(
                userId = updatedEmployee.userId,
                email = employee.email,
                role = employee.role,
                userName = updatedEmployee.userName.takeIf { it.isNotBlank() } ?: employee.userName,
                workspace = updatedEmployee.workspace.takeIf { it.isNotBlank() } ?: employee.workspace,
                organizationId = employee.organizationId,
                branch = employee.branch,
                position = updatedEmployee.position,
                startedAt = employee.startedAt,
                endedAt = employee.endedAt
            )

            completeEmployee
        }
    }
}