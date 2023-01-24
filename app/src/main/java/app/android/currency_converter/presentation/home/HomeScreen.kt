package app.android.currency_converter.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.android.currency_converter.R
import app.android.currency_converter.core.theme.black
import app.android.currency_converter.core.theme.blue
import app.android.currency_converter.presentation.common_components.*
import app.android.currency_converter.presentation.utils.showToast

/**
 * Home screen composable
 * @param viewModel variable for home view model
 * @param hasNetwork variable used to identify whether device has valid internet connection
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel, hasNetwork: Boolean) {

    val currencyText by viewModel.currentCurrencyInput.collectAsStateWithLifecycle()
    val currencyList by viewModel.countryList.collectAsStateWithLifecycle()
    val exchangeRateList by viewModel.latestExchangeRateList.collectAsStateWithLifecycle()
    val textFieldCurrencyLabel by viewModel.currentBaseCountry.collectAsStateWithLifecycle()

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(blue)
            .padding(all = dimensionResource(id = R.dimen.dp25))
    ) {
        val (titleRef, textFieldRef,
            dropDownRef, loaderRef,
            networkNotAvailableRef,
            lazyRef) = createRefs()

        val dp10 = dimensionResource(id = R.dimen.dp10)
        val dp20 = dimensionResource(id = R.dimen.dp20)

        Title(text = stringResource(R.string.homeTitle),
            modifier = Modifier.constrainAs(titleRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top, dp20)
                end.linkTo(parent.end)
            })

        CurrencyInput(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(textFieldRef) {
                start.linkTo(parent.start)
                top.linkTo(titleRef.bottom, dp20)
                end.linkTo(parent.end)
            },
            defaultText = currencyText,
            currencySymbol = textFieldCurrencyLabel,
            onTextChange = { value ->
                viewModel.onCurrencyInputChange(value)
            },
            onTextInputError = {
                context.showToast(it)
            })


        DropdownMenu(modifier = Modifier
            .fillMaxWidth(0.7f)
            .constrainAs(dropDownRef) {
                top.linkTo(
                    textFieldRef.bottom,
                    dp10
                )
                end.linkTo(parent.end)
            },
            items = currencyList,
            defaultText = stringResource(R.string.tempLabel),
            onItemSelected = {
                viewModel.onCountrySelected(it)
            })

        AnimatedVisibility(
            visible = !hasNetwork,
            modifier = Modifier.constrainAs(networkNotAvailableRef) {
                start.linkTo(parent.start)
                top.linkTo(dropDownRef.bottom, dp20)
                end.linkTo(parent.end)
            }, enter = fadeIn(), exit = fadeOut()
        ) {
            ErrorText(text = stringResource(id = R.string.notNetworkConnection))
        }

        AnimatedVisibility(
            visible = viewModel.isLoading,
            modifier = Modifier.constrainAs(loaderRef) {
                start.linkTo(parent.start)
                top.linkTo(
                    if (!hasNetwork) networkNotAvailableRef.bottom else dropDownRef.bottom,
                    dp20
                )
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }, enter = fadeIn(), exit = fadeOut()
        ) {
            LinearProgressbar(color = black)
        }

        AnimatedVisibility(
            visible = !viewModel.isLoading,
            modifier = Modifier.constrainAs(lazyRef) {
                start.linkTo(parent.start)
                top.linkTo(
                    if (!hasNetwork) networkNotAvailableRef.bottom else dropDownRef.bottom,
                    dp20
                )
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }, enter = fadeIn(), exit = fadeOut()
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(dp10),
                content = {
                    items(exchangeRateList) {
                        CurrencyItem(currency = it, countryName = viewModel.getCountryName(it.code))
                    }
                })

        }

        // launched effect block to load data from network if the network is becomes available
        LaunchedEffect(key1 = hasNetwork, block = {
            // if currency list is empty then it means, app is launched with no internet connection
            if (hasNetwork && currencyList.isEmpty()) {
                viewModel.fetchOrLoadLocally()
            }
        })


    }

}