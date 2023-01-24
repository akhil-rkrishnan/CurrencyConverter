package app.android.currency_converter.presentation.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.TextFieldValue
import app.android.currency_converter.R

private val osProperty: String = System.getProperty("os.name", "unknown") as String
private val isMac = osProperty.startsWith("mac", ignoreCase = true)

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.persistShortKeyForInput(input: TextFieldValue): Modifier {
    return onPreviewKeyEvent { event ->
        // Returns true to mark the event as handled and stop its propagation.
        when {
            // Temporary fix for https://github.com/JetBrains/compose-jb/issues/565.
            // To repro, press CTRL/Option+backspace on an empty TextField.
            event.isCtrlBackspace() && input.text.isEmpty() -> true
            // Temporary fix for https://github.com/JetBrains/compose-jb/issues/2023.
            // To repro, simply press the DEL key at the end of the text.
            event.key == Key.Delete && input.isCursorAtTheEnd() -> true
            else -> false
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun KeyEvent.isCtrlBackspace() =
    (key == Key.Backspace || key == Key.Enter || key == Key.Delete
            || key == Key.DirectionUp || key == Key.DirectionDown
            || key == Key.DirectionLeft || key == Key.DirectionRight) && ((isCtrlPressed || isAltPressed) || (isMac && isAltPressed))


private fun TextFieldValue.isCursorAtTheEnd(): Boolean {
    val hasNoSelection = selection.collapsed
    val isCursorAtTheEnd = text.length == selection.end
    return hasNoSelection && isCursorAtTheEnd
}

fun Context.showToast(message: String, longToast: Boolean = false) {
    Toast.makeText(this, message, if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: UiText?, longToast: Boolean = false) {
    val toastDuration = if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    when (message) {
        is UiText.DynamicString -> {
            Toast.makeText(this, message.message, toastDuration).show()
        }
        is UiText.StringResId -> {
            if (message.resID != null) {
                Toast.makeText(this, getString(message.resID), toastDuration).show()
            } else {
                Toast.makeText(this, getString(R.string.invalidMessage), toastDuration).show()
            }
        }
        else -> {
            Toast.makeText(this, getString(R.string.invalidMessage), toastDuration).show()
        }
    }
}