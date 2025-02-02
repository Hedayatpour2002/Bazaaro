package com.example.bazaaro.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.app.DETAIL_SCREEN_ROUTE
import com.example.bazaaro.app.ui.components.AlertDialogView
import com.example.bazaaro.app.ui.components.DashedDivider
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.app.ui.components.ProductQuantitySectionView
import com.example.bazaaro.data.model.CartEntity


@Composable
fun CartScreen(cartViewModel: CartViewModel = hiltViewModel(), navController: NavHostController) {
    val cartState = cartViewModel.cartState.collectAsStateWithLifecycle()

    when (val state = cartState.value) {
        CartState.Loading -> LoadingView()
        is CartState.Success -> CartView(products = state.products, onIncrement = {
            cartViewModel.addToCart(it)
        }, onDecrement = {
            cartViewModel.removeOrDecrementProduct(it)
        }, removeAllFromCart = {
            cartViewModel.removeAllFromCart()
        }, removeFromCart = {
            cartViewModel.removeFromCart(it)
        }) { clickedItemId ->
            navController.navigate("$DETAIL_SCREEN_ROUTE/$clickedItemId")
        }

        is CartState.Error -> ErrorView(errorMessage = state.errorMessage) {
            cartViewModel.getCartProducts()
        }

        CartState.EmptyList -> EmptyListView()
    }
}

@Composable
fun EmptyListView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cart_is_empty))

    Text(
        text = stringResource(R.string.cart),
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        color = Color(0xFF222222),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, top = 8.dp)
    )
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
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
        AlertDialogView(
            onDismissRequestText = stringResource(R.string.cancel),
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmationText = stringResource(R.string.confirm),
            onConfirmation = {
                removeAllFromCart()
                openAlertDialog.value = false
            },
            dialogTitle = stringResource(R.string.removeDialogTitle),
            dialogText = stringResource(R.string.removeCartDialogText),
            icon = Icons.Default.Delete
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.cart),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF222222),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    openAlertDialog.value = true
                }) {
                    Text(
                        text = stringResource(R.string.removeAll),
                        textAlign = TextAlign.End,
                        color = Color(0xFF838383),
                    )
                }
            }
        }

        items(products) {
            CartProductView(it, onItemClick, onIncrement, onDecrement) {
                removeFromCart(it)
            }
        }

        item {
            PaymentSummaryView(products)
        }
    }
}

@Composable
fun CartProductView(
    product: CartEntity,
    onItemClick: (Int) -> Unit,
    onIncrement: (CartEntity) -> Unit,
    onDecrement: (CartEntity) -> Unit,
    removeFromCart: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = removeFromCart,
            modifier = Modifier
                .offset(x = 0.dp, y = 0.dp)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                .size(32.dp)
                .zIndex(1f)
                .background(Color.Black)
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = Color(0xFFF4F4F4)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AsyncImage(model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .padding(vertical = 18.dp, horizontal = 8.dp)
                    .clickable { onItemClick(product.id) })

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 18.dp, end = 8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(product.title,
                        modifier = Modifier
                            .clickable { onItemClick(product.id) }
                            .weight(1f),
                        color = Color(0xFF272727),
                        fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${"%.2f".format(product.price * product.quantity)}",
                        color = Color(0xFFDB3022),
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = product.category, fontSize = 14.sp, modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ProductQuantitySectionView(quantity = product.quantity,
                        onIncrement = { onIncrement(product) },
                        onDecrement = { onDecrement(product) })
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun PaymentSummaryView(products: List<CartEntity>) {
    var totalPrice = 0.0
    products.forEach {
        totalPrice += it.price * it.quantity
    }
    val totalPriceStr = "%.2f".format(totalPrice)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.payment_summary),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.subtotal),
                color = Color(0xFF838383),
                fontSize = 14.sp
            )
            Text(
                text = "$${totalPriceStr}", color = Color(0xFF838383), fontSize = 14.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.discount),
                color = Color(0xFF838383),
                fontSize = 14.sp
            )
            Text(
                text = "$0", color = Color(0xFF838383), fontSize = 14.sp
            )
        }

        DashedDivider()

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.total_amount), fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "$${totalPriceStr}", fontWeight = FontWeight.ExtraBold
            )
        }

        Button(
            onClick = { /* TODO: Handle Checkout */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDB3022), contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.checkout),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}