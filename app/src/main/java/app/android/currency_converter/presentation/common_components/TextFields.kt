package app.android.currency_converter.presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.android.currency_converter.R
import app.android.currency_converter.core.theme.black
import app.android.currency_converter.core.theme.grey
import app.android.currency_converter.core.theme.white
import app.android.currency_converter.data.utils.isValidNumber
import app.android.currency_converter.presentation.utils.fontDimensionResource
import app.android.currency_converter.presentation.utils.persistShortKeyForInput

/**
* Composable for currency input field
 * @param modifier modifier for the composable
 * @param defaultText default text for the field
 * @param currencySymbol default currency symbol
 * @param numberOnly to accept only numbers for input
 * @param onTextChange lambda for text change call back
 * @param onTextInputError lambda for text input error call back
 * @param onSubmitText lambda for submit text call back
*/
@Composable
fun CurrencyInput(
    modifier: Modifier = Modifier,
    defaultText: String = "",
    currencySymbol: String = "USD",
    numberOnly: Boolean = true,
    onTextChange: (String) -> Unit,
    onTextInputError: (String) -> Unit,
    onSubmitText: (String) -> Unit = {}
) {
    var inputText by remember {
        mutableStateOf(TextFieldValue(defaultText))
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .height(
                dimensionResource(id = R.dimen.textFieldHeight)
            )
            .background(white, RoundedCornerShape(10.dp))
            .height(
                dimensionResource(id = R.dimen.textFieldHeight)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { value ->
                if (numberOnly && value.text.isNotEmpty()) {
                    if (value.text.isValidNumber()) {
                        inputText = value
                        onTextChange(inputText.text)
                    } else {
                        onTextInputError(context.getString(R.string.invalidNumberError))
                    }
                } else {
                    inputText = value
                    onTextChange(inputText.text)
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .persistShortKeyForInput(inputText),
            keyboardActions = KeyboardActions(onDone = {
                onSubmitText(inputText.text)
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = white,
                unfocusedIndicatorColor = white,
                cursorColor = black,
                backgroundColor = white,
            ),
            textStyle = LocalTextStyle.current.copy(
                color = black,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = fontDimensionResource(id = R.dimen.sp20)
            )
        )
        Text(
            text = currencySymbol,
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.dp20)),
            style = LocalTextStyle.current.copy(
                color = grey,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontSize = fontDimensionResource(id = R.dimen.sp14)
            )

        )

    }
}