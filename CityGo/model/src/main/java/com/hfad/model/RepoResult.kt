package com.hfad.model

sealed class RepoResult<out T : Any>
data class Success<out T : Any>(val data: T) : RepoResult<T>()
data class Failure(val error: BasicError) : RepoResult<Nothing>()
data class Progress<out T : Any>(val progress: T) : RepoResult<T>()


enum class ErrorCode(val code: Int) {
    ERROR(0),
    ERROR_1(1),
    ERROR_2(2);

    companion object {
        infix fun from(code: Int): ErrorCode? = values().firstOrNull { it.code == code }
    }

}

class BasicError(val throwable: Throwable, val errorCode: ErrorCode = ErrorCode.ERROR, val cardImg: Int? = null, val rockValue: String? = null, val rockStatus: Int? = null, val rockMSG: String? = null)
class HttpError(val throwable: Throwable, val errorCode: Int = 0)

inline fun <T : Any> RepoResult<T>.onSuccess(action: (T) -> Unit): RepoResult<T> {
    if (this is Success) {
        action(data)
    }
    return this
}

inline fun <T : Any> RepoResult<T>.onFailure(action: (BasicError) -> Unit) {
    if (this is Failure) action(error)
}

inline fun <R : Any> RepoResult<R>.onProgress(action: (Any) -> Unit): RepoResult<R> {
    if (this is Progress) action(progress)
    return this


}