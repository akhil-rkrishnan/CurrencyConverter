package app.android.currency_converter.data.utils

import app.android.currency_converter.presentation.utils.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Method to execute the Network call.
 * @param block lambda defined for the user api call block
 * @return ApiResult<Type> The response body / exception
 */
suspend fun <M> initApiCall(block: suspend () -> ApiResult<M>): ApiResult<M> = withContext(Dispatchers.IO) {
    try {
        block()
    } catch (ex: Exception) {
        ApiResult.Failed(UiText.DynamicString(ex.message))
    }
}

sealed class ApiResult<out Model> {
    object None : ApiResult<Nothing>()
    data class Success<out Model>(val data: Model) : ApiResult<Model>()
    data class Failed(val uiText: UiText?) : ApiResult<Nothing>()
}

inline fun <reified Model> ApiResult<Model>.ifSuccess(block: (data: Model) -> Unit) {
    if (this is ApiResult.Success) {
        block(data)
    }
}

inline fun <reified Model> ApiResult<Model>.ifFailed(cause: (message: UiText?) -> Unit) {
    if (this is ApiResult.Failed) {
        cause(uiText)
    }
}
