package app.android.currency_converter.presentation.common_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import app.android.currency_converter.R
import app.android.currency_converter.core.theme.green
import app.android.currency_converter.core.theme.grey
import app.android.currency_converter.data.utils.formattedDouble
import app.android.currency_converter.presentation.utils.fontDimensionResource

/**
* Method to format the current input
 * @param rate latest calculated rate
 * @param code Currency code
 * @return Annotated string after appending custom style.
*/
@Composable
fun formatAsCurrency(rate: Double, code: String): AnnotatedString {
    val spanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
        fontStyle = FontStyle(100),
        fontSize = fontDimensionResource(id = R.dimen.sp16)
    )
    return buildAnnotatedString {
        withStyle(
            style = spanStyle.copy(color = green)
        ) {
            append(rate.formattedDouble())
        }
        append(" ")
        withStyle(style = spanStyle.copy(color = grey)) {
            append(code)
        }
    }
}