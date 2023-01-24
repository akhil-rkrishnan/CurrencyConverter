package app.android.currency_converter.presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.android.currency_converter.R
import app.android.currency_converter.core.theme.black
import app.android.currency_converter.core.theme.white
import app.android.currency_converter.data.local.Currency
import app.android.currency_converter.presentation.utils.fontDimensionResource

/**
* Lazy list item composable
 * @param currency currency object
 * @param countryName country name
*/
@Composable
fun CurrencyItem(currency: Currency, countryName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8))
            .background(color = white, shape = RoundedCornerShape(8)),
        elevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.dp6)
            ), modifier = Modifier.padding(
                all = dimensionResource(
                    id = R.dimen.dp10
                )
            )
        ) {
            Text(
                text = countryName,
                style = LocalTextStyle.current.copy(
                    color = black,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = fontDimensionResource(id = R.dimen.sp16)
                ), overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatAsCurrency(rate = currency.rate, code = currency.code)
            )
        }
    }
}
