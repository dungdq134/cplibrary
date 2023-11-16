package pl.cyfrowypolsat.cpdata.api.common.extensions

import retrofit2.HttpException
import retrofit2.adapter.rxjava3.Result

fun <T> Result<T>.handleError() {
    error()?.let { error -> throw error }
    if (response()?.isSuccessful == false) {
        throw HttpException(response())
    }
}