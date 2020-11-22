package dev.ohoussein.restos.ui.core.mapper

import android.content.Context
import dev.ohoussein.restos.R
import java.io.IOException

class ThrowableMessageMapper(private val context: Context) {

    fun errorMessage(exception: Throwable?): String {
        return when (exception) {
            is IOException -> context.getString(R.string.error_no_internet_connection)
            else -> {
                if (exception?.message != null)
                    context.getString(R.string.error_unknown_error_with_message, exception.message)
                else
                    context.getString(R.string.error_unknown_error)
            }
        }
    }
}
