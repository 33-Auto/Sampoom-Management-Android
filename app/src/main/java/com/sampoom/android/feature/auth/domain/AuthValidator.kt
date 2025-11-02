package com.sampoom.android.feature.auth.domain

import com.sampoom.android.R

object AuthValidator {
    // 이메일 형식 검증
    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Error(R.string.validation_email_required)
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        if (!email.matches(emailPattern)) {
            return ValidationResult.Error(R.string.validation_email_invalid)
        }

        return ValidationResult.Success
    }

    // 비밀번호 검증 (8-30자, 영문+숫자+특수문자 각 1개 이상)
    fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult.Error(R.string.validation_password_required)
        }

        if (password.length < 8) {
            return ValidationResult.Error(R.string.validation_password_min_length)
        }

        if (password.length > 30) {
            return ValidationResult.Error(R.string.validation_password_max_length)
        }

        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        if (!hasLetter || !hasDigit || !hasSpecial) {
            return ValidationResult.Error(R.string.validation_password_complexity)
        }

        return ValidationResult.Success
    }

    // 비밀번호 확인 검증
    fun validatePasswordCheck(password: String, passwordConfirm: String): ValidationResult {
        if (passwordConfirm.isBlank()) {
            return ValidationResult.Error(R.string.validation_password_confirm_required)
        }

        if (password != passwordConfirm) {
            return ValidationResult.Error(R.string.validation_password_mismatch)
        }

        return ValidationResult.Success
    }

    // 일반 필드 검증 (이름, 지점, 직책 등)
    fun validateNotEmpty(value: String, fieldName: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult.ErrorWithArgs(R.string.validation_field_required, fieldName)
        }
        return ValidationResult.Success
    }
}

// 검증 결과
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val messageResId: Int) : ValidationResult()
    data class ErrorWithArgs(val messageResId: Int, val args: Any) : ValidationResult()
}