package pl.cyfrowypolsat.cpplayerexternal.presentation.error

import android.content.Context
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayerexternal.R
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

object ErrorMessageProvider {

    fun getMessage(throwable: Throwable, context: Context): String {
        Timber.e(throwable)
        return when (throwable) {
            is IOException -> {
                context.getString(R.string.cppl_error_no_internet)
            }
            is HttpException -> {
                getHttpExceptionMessage(throwable, context)
            }
            is PlayerException -> {
                getPlayerExceptionMessage(throwable, context)
            }
            else -> {
                context.getString(R.string.cppl_error_unknown_error)
            }
        }
    }

    private fun getHttpExceptionMessage(httpException: HttpException, context: Context): String {
        return context.getString(R.string.cppl_error_with_code).format(httpException.code())
    }

    private fun getPlayerExceptionMessage(playerException: PlayerException,
                                          context: Context): String {
        return context.getString(R.string.cppl_error_with_code, playerException.code)
    }

}