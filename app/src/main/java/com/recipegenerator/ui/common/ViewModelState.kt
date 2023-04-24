package com.recipegenerator.ui.common

data class ViewModelState<out T>(
    val status: Status,
    val data: T? = null,
    val message: String = "",
    val exception: Throwable? = null
) {

    companion object {
        fun <T> success(data: T): ViewModelState<T> =
            ViewModelState(Status.SUCCESS, data)

        fun <T> error(message: String, exception: Throwable? = null): ViewModelState<T> =
            ViewModelState(Status.ERROR, message = message, exception = exception)

        fun <T> loading(): ViewModelState<T> = ViewModelState(status = Status.LOADING)
    }

}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}