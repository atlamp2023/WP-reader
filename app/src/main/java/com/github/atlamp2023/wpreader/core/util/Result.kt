package com.github.atlamp2023.wpreader.core.util

sealed class Result <T> {
    class Success<T>(
        val value: T
    ) : Result<T>()

    class Error<T>(
        val error: Throwable? = null
    ) : Result<T>()

    class Empty<T> : Result<T>()

    class Pending<T> : Result<T>()
}