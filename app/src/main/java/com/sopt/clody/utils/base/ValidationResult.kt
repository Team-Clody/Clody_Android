package com.sopt.clody.utils.base

sealed class ValidationResult {
    object EmptyError : ValidationResult()
    object Error : ValidationResult()
    object Success : ValidationResult()

    companion object {
        fun from(isValid: Boolean) = if (isValid) Success else Error
    }
}
