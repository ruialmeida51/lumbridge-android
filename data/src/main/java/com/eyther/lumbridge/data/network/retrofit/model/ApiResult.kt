package com.eyther.lumbridge.data.network.retrofit.model

sealed class ApiResult<out T> {
    /**
     * Represents a successful outcome.
     *
     * @param data The encapsulated value.
     */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * Represents a failed outcome.
     *
     * @param exception The encapsulated [Throwable] exception.
     */
    sealed class Error(
        open val exception: Throwable? = null,
        open val code: Int? = null
    ) : ApiResult<Nothing>() {
        data class RequestError(
            override val exception: Throwable,
            override val code: Int
        ) : Error(exception)

        data class NetworkError(
            override val exception: Throwable
        ) : Error(exception)

        data class ServerError(
            override val exception: Throwable
        ) : Error(exception)
    }

    /**
     * Represents a loading state.
     */
    data object Loading : ApiResult<Nothing>()

    /**
     * Returns `true` if this instance represents a successful outcome.
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Returns `true` if this instance represents a failed outcome.
     */
    fun isError(): Boolean = this is Error

    /**
     * Returns `true` if this instance represents a failed outcome.
     */
    fun isLoading(): Boolean = this is Loading

    /**
     * Returns the encapsulated value if this instance represents [success][ApiResult.isSuccess] or `null`
     * if it is [failure][ApiResult.isError].
     */
    fun getOrNull(): T? =
        when (this) {
            is Success -> this.data
            else -> null
        }

    /**
     * Returns the encapsulated [Throwable] exception if this instance represents [failure][isError] or `null`
     * if it is [success][isSuccess].
     */
    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Error -> this.exception
            else -> null
        }
}
