package com.sampoom.android.core.network

import com.sampoom.android.core.preferences.AuthPreferences
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {
    private val refreshMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        // 이미 재시도된 요청인지 확인
        if (response.request.header("X-Retry-Count") != null) {
            return null // 재시도 제한
        }

        return try {
            val newUser = runBlocking {
                refreshMutex.withLock {
                    tokenRefreshService.refreshToken().getOrThrow()
                }
            }

            response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${newUser.accessToken}")
                .addHeader("X-Retry-Count", "1")
                .build()
        } catch (e: retrofit2.HttpException) {
            // HTTP 오류별 분기 처리
            when (e.code()) {
                400, 401 -> {
                    // 인증 실패: 토큰 삭제
                    runBlocking { authPreferences.clear() }
                    null
                }
                403 -> {
                    // 권한 없음: 토큰 삭제
                    runBlocking { authPreferences.clear() }
                    null
                }
                429 -> {
                    // Rate Limit: 토큰 보존, 재시도는 호출자 판단
                    null
                }
                in 500..599 -> {
                    // 서버 오류: 토큰 보존
                    null
                }
                else -> {
                    // 기타 HTTP 오류: 토큰 보존
                    null
                }
            }
        } catch (e: java.io.IOException) {
            // 네트워크 일시 오류: 토큰 보존, 재시도는 호출자 판단
            null
        } catch (e: java.net.SocketTimeoutException) {
            // 타임아웃: 토큰 보존
            null
        } catch (e: java.net.UnknownHostException) {
            // DNS 오류: 토큰 보존
            null
        } catch (e: java.net.ConnectException) {
            // 연결 오류: 토큰 보존
            null
        } catch (t: Throwable) {
            // 기타 예외: 토큰 보존
            null
        }
    }

    private suspend fun isTokenExpired(token: String): Boolean {
        // 간단한 토큰 만료 체크 (JWT 디코딩 없이)
        return try {
            val expiresAt = authPreferences.isTokenExpired()
            expiresAt
        } catch (e: Exception) {
            true // 파싱 실패 시 만료로 간주
        }
    }
}