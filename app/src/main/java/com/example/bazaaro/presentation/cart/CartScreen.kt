package com.example.bazaaro.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.app.DETAIL_SCREEN_ROUTE
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.presentation.cart.components.CartHeader
import com.example.bazaaro.presentation.cart.components.CartProductItem
import com.example.bazaaro.presentation.cart.components.PaymentSummaryView
import com.example.bazaaro.presentation.cart.components.RemoveAllDialog


@Composable
fun CartScreen(cartViewModel: CartViewModel = hiltViewModel(), navController: NavHostController) {
    val cartState = cartViewModel.cartState.collectAsStateWithLifecycle()

    when (val state = cartState.value) {
        CartState.Loading -> LoadingView()
        is CartState.Success -> CartView(products = state.products,
            onIncrement = { cartViewModel.addToCart(it) },
            onDecrement = { cartViewModel.removeOrDecrementProduct(it) },
            removeAllFromCart = { cartViewModel.removeAllFromCart() },
            removeFromCart = { cartViewModel.removeFromCart(it) },
            onItemClick = { clickedItemId -> navController.navigate("$DETAIL_SCREEN_ROUTE/$clickedItemId") })

        is CartState.Error -> ErrorView(errorMessage = state.errorMessage) { cartViewModel.getCartProducts() }
        CartState.EmptyList -> EmptyListView()
    }
}

@Composable
fun EmptyListView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cart_is_empty))

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.cart),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF222222),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
        Text(
            text = stringResource(R.string.cart_empty),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun CartView(
    products: List<CartEntity>,
    onIncrement: (CartEntity) -> Unit,
    onDecrement: (CartEntity) -> Unit,
    removeAllFromCart: () -> Unit,
    removeFromCart: (CartEntity) -> Unit,
    onItemClick: (Int) -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        RemoveAllDialog(onDismissRequest = { openAlertDialog.value = false }, onConfirmation = {
            removeAllFromCart()
            openAlertDialog.value = false
        })
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CartHeader(onRemoveAllClick = { openAlertDialog.value = true })
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                CartProductItem(product = product,
                    onItemClick = onItemClick,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement,
                    removeFromCart = { removeFromCart(product) })
            }
            item {
                PaymentSummaryView(products = products)
            }
        }
    }
}