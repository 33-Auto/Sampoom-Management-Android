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
import com.sampoom.android.feature.user.data.remote.dto.UpdateEmployeeStatusRequestDto
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
    /** 유저 프로필 조회 */
    override suspend fun getStoredUser(): User? {
        return preferences.getStoredUser()
    }

    /** 프로필 조회 */
    override suspend fun getProfile(role: String): Result<User> {
        return runCatching {
            retry(times = 5, initialDelay = 300) {
                val dto = api.getProfile(role)
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

    /** 프로필 수정 */
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

    /** 직원 목록 조회 */
    override fun getEmployeeList(): Flow<PagingData<Employee>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceFactory.create() }
        ).flow
    }

    /** 직원 프로필 수정 */
    override suspend fun editEmployee(
        employee: Employee,
        role: String
    ): Result<Employee> {
        return runCatching {
            val requestDto = EditEmployeeRequestDto(
                position = employee.position.name
            )
            val dto = api.editEmployee(
                userId = employee.userId,
                role = role,
                body = requestDto
            )
            if (!dto.success) throw Exception(dto.message)

            val updatedEmployee = dto.data.toModel()
            val completeEmployee = Employee(
                userId = updatedEmployee.userId,
                email = employee.email,
                role = updatedEmployee.role.takeIf { it.isNotBlank() } ?: employee.role,
                userName = updatedEmployee.userName.takeIf { it.isNotBlank() } ?: employee.userName,
                organizationId = employee.organizationId,
                branch = employee.branch,
                position = updatedEmployee.position,
                status = employee.status,
                createdAt = employee.createdAt,
                startedAt = employee.startedAt,
                endedAt = employee.endedAt,
                deletedAt = employee.deletedAt
            )

            completeEmployee
        }
    }

    /** 직원 상태 수정 */
    override suspend fun updateEmployeeStatus(
        employee: Employee,
        role: String
    ): Result<Employee> {
        return runCatching {
            val requestDto = UpdateEmployeeStatusRequestDto(
                employeeStatus = employee.status.name
            )
            val dto = api.updateEmployeeStatus(
                userId = employee.userId,
                role = role,
                body = requestDto
            )
            if (!dto.success) throw Exception(dto.message)

            val updateEmployeeStatus = dto.data.toModel()
            val completedEmployeeStatus = Employee(
                userId = updateEmployeeStatus.userId,
                email = employee.email,
                role = updateEmployeeStatus.role.takeIf { it.isNotBlank() } ?: employee.role,
                userName = updateEmployeeStatus.userName.takeIf { it.isNotBlank() } ?: employee.userName,
                organizationId = employee.organizationId,
                branch = employee.branch,
                position = employee.position,
                status = updateEmployeeStatus.status,
                createdAt = employee.createdAt,
                startedAt = employee.startedAt,
                endedAt = employee.endedAt,
                deletedAt = employee.deletedAt
            )

            completedEmployeeStatus
        }
    }

    /** 직원 수 조회 */
    override suspend fun getEmployeeCount(): Result<Int> {
        return runCatching {
            val user = preferences.getStoredUser() ?: throw Exception()
            val role = user.role
            val organizationId = user.agencyId

            val dto = api.getEmployeeList(
                role = role,
                organizationId = organizationId,
                page = 0,
                size = 1
            )

            if (!dto.success) throw Exception(dto.message)
            dto.data.meta.totalElements
        }
    }
}