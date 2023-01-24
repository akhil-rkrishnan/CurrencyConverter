package app.android.currency_converter.presentation.common_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.toSize
import app.android.currency_converter.R
import app.android.currency_converter.core.theme.black
import app.android.currency_converter.core.theme.white
import app.android.currency_converter.data.local.Country
import app.android.currency_converter.presentation.utils.fontDimensionResource
import kotlinx.coroutines.flow.collectLatest

/**
* Drop down menu composable
 * @param modifier Modifier for the composable
 * @param items list of countries
 * @param onItemSelected lambda for item selection callback
 * @param defaultText default text displayed on drop down
 * @param hint hint for the drop down menu
 * @param disableFocusColor disable focus color
 * @param content lambda for user defined composable content
*/
@Composable
fun DropdownMenu(
    modifier: Modifier = Modifier,
    items: List<Country>,
    onItemSelected: (Country) -> Unit,
    defaultText: String = "",
    hint: String = stringResource(R.string.selectAnItem),
    disableFocusColor: Boolean = false,
    content: (@Composable (label: String) -> Unit)? = null
) {

    var expanded by remember { mutableStateOf(false) }

    var currentText by remember {
        mutableStateOf(defaultText)
    }
    var textfieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(key1 = Unit) {
        interactionSource.interactions.collectLatest { interactions ->
            when (interactions) {
                is PressInteraction.Press -> {}
                is PressInteraction.Release -> {
                    expanded = !expanded
                }
                is PressInteraction.Cancel -> {
                    expanded = false
                }
            }
        }
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = currentText,
            interactionSource = interactionSource,
            readOnly = true,
            onValueChange = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (disableFocusColor) white else black,
            ),
            trailingIcon = {
                Icon(icon, stringResource(R.string.contentDescription),
                    Modifier
                        .size(dimensionResource(id = R.dimen.dp32))
                        .clickable { expanded = !expanded })
            },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp12)),
            placeholder = {
                Text(text = hint)
            }, maxLines = 1, singleLine = true, textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = fontDimensionResource(id = R.dimen.sp14)
            ),

        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            items.forEach { country ->
                DropdownMenuItem(onClick = {
                    currentText = country.name
                    onItemSelected(country)
                    expanded = false
                }) {
                    if (content != null) {
                        content(country.name)
                    } else {
                        Text(text = country.name, style = LocalTextStyle.current.copy(
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        ))
                    }
                }
            }
        }
    }

}