package app.android.currency_converter.presentation.common_components

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import app.android.currency_converter.R
import app.android.currency_converter.core.theme.black
import app.android.currency_converter.core.theme.orange
import app.android.currency_converter.core.theme.red
import app.android.currency_converter.presentation.utils.fontDimensionResource

/**
* Composable for screen title
 * @param modifier modifier for the composable
 * @param text text to display
*/
@Composable
fun Title(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier, text = text, color = black,
        style = LocalTextStyle.current.copy(
            color = red,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            fontSize = fontDimensionResource(id = R.dimen.sp22)
        )
    )
}

/**
* Composable for error text
 * @param modifier modifier for the composable
 * @param text text to display
*/
@Composable
fun ErrorText(modifier : Modifier = Modifier, text: String) {
    Text(
        modifier = modifier, text = text, color = orange,
        style = LocalTextStyle.current.copy(
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontSize = fontDimensionResource(id = R.dimen.sp12)
        )
    )
}
