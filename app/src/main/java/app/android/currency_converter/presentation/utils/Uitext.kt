package app.android.currency_converter.presentation.utils

/**
* Sealed class for UiText
*/
sealed class UiText {
    data class DynamicString(val message: String?) : UiText()
    data class StringResId(val resID: Int?) : UiText()
}