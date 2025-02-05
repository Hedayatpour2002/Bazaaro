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
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.bazaaro.app.ui.components.ModalBottomSheetView
import com.example.bazaaro.app.ui.components.ProductQuantitySectionView
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.model.Coupon
import kotlinx.coroutines.launch


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


@Composable
fun CartHeader(onRemoveAllClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.cart),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF222222),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onRemoveAllClick) {
                Text(
                    text = stringResource(R.string.removeAll),
                    textAlign = TextAlign.End,
                    color = Color(0xFF838383),
                )
            }
        }
    }
}

@Composable
fun RemoveAllDialog(onDismissRequest: () -> Unit, onConfirmation: () -> Unit) {
    AlertDialogView(
        onDismissRequestText = stringResource(R.string.cancel),
        onDismissRequest = onDismissRequest,
        onConfirmationText = stringResource(R.string.confirm),
        onConfirmation = onConfirmation,
        dialogTitle = stringResource(R.string.removeDialogTitle),
        dialogText = stringResource(R.string.removeCartDialogText),
        icon = Icons.Default.Delete
    )
}

@Composable
fun CartProductItem(
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
                contentDescription = "Remove from cart",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        ProductCard(
            product = product,
            onItemClick = onItemClick,
            onIncrement = onIncrement,
            onDecrement = onDecrement
        )
    }
}

@Composable
fun ProductCard(
    product: CartEntity,
    onItemClick: (Int) -> Unit,
    onIncrement: (CartEntity) -> Unit,
    onDecrement: (CartEntity) -> Unit
) {
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
            ProductTitleAndPrice(product = product, onItemClick = onItemClick)
            ProductCategoryAndQuantity(
                product = product, onIncrement = onIncrement, onDecrement = onDecrement
            )
        }
    }
}

@Composable
fun ProductTitleAndPrice(product: CartEntity, onItemClick: (Int) -> Unit) {
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
}

@Composable
fun ProductCategoryAndQuantity(
    product: CartEntity, onIncrement: (CartEntity) -> Unit, onDecrement: (CartEntity) -> Unit
) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSummaryView(products: List<CartEntity>) {
    val totalPrice = remember(products) {
        products.sumOf { it.price * it.quantity }
    }
    val totalPriceStr = "%.2f".format(totalPrice)

    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheetView(showBottomSheet = showBottomSheet, sheetState = sheetState) {
        CouponBottomSheetContent {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet.value = false
                }
            }
        }
    }


    PaymentSummaryContent(totalPriceStr = totalPriceStr,
        onCouponClick = { showBottomSheet.value = !showBottomSheet.value },
        onCheckoutClick = { /* TODO: Handle Checkout */ })
}

@Composable
fun PaymentSummaryContent(
    totalPriceStr: String, onCouponClick: () -> Unit, onCheckoutClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.payment_summary),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        SummaryRow(label = stringResource(R.string.subtotal), value = "$${totalPriceStr}")
        SummaryRow(label = stringResource(R.string.discount), value = "$0")

        DashedDivider()

        SummaryRow(
            label = stringResource(R.string.total_amount),
            value = "$${totalPriceStr}",
            isTotal = true
        )

        CouponButton(onClick = onCouponClick)

        Spacer(modifier = Modifier.height(8.dp))

        CheckoutButton(onClick = onCheckoutClick)
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (isTotal) Color(0xFF1B1B1B) else Color(0xFF838383),
            fontSize = 14.sp,
            fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.Normal
        )
        Text(
            text = value,
            color = if (isTotal) Color(0xFF1B1B1B) else Color(0xFF838383),
            fontSize = 14.sp,
            fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}

@Composable
fun CouponButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.offer))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LottieAnimation(
                    composition = composition,
                    isPlaying = true,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 16.dp)
                )

                Text(
                    text = stringResource(R.string.do_you_have_coupon_code), color = Color(0xFF838383), fontSize = 12.sp
                )
            }

            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Apply Coupon",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun CheckoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
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

@Composable
fun CouponBottomSheetContent(onApplyCoupon: () -> Unit) {
    val fakeCouponList = remember {
        listOf(
            Coupon(
                discountPercentage = "15",
                offerTitle = "Summer Sale",
                couponCode = "SUMMER15",
                daysRemaining = "10 days remaining"
            ), Coupon(
                discountPercentage = "20",
                offerTitle = "First Order Discount",
                couponCode = "WELCOME20",
                daysRemaining = "30 days remaining"
            ), Coupon(
                discountPercentage = "10",
                offerTitle = "Loyalty Reward",
                couponCode = "LOYALTY10",
                daysRemaining = "7 days remaining"
            )
        )
    }

    val promoCodeInput = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100))
                    .background(Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.offer))
                    LottieAnimation(
                        composition = composition,
                        isPlaying = true,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedTextField(
                        value = promoCodeInput.value,
                        onValueChange = {
                            promoCodeInput.value = it
                        },
                        placeholder = {
                            Text(text = stringResource(R.string.enter_coupon_code))
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF838383),
                            unfocusedTextColor = Color(0xFF838383),
                            focusedPlaceholderColor = Color(0xFF838383),
                            unfocusedPlaceholderColor = Color(0xFF838383),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(
                                RoundedCornerShape(100)
                            )
                            .background(Color.Black)
                            .clickable {
                                onApplyCoupon()
                            })
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(text = stringResource(R.string.your_promo_codes), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
        }
        items(fakeCouponList) { couponData ->
            CouponCard(discountPercentage = couponData.discountPercentage,
                offerTitle = couponData.offerTitle,
                couponCode = couponData.couponCode,
                daysRemaining = couponData.daysRemaining,
                onApplyClick = { promoCodeInput.value = couponData.couponCode })
        }
    }
}

@Composable
fun CouponCard(
    discountPercentage: String,
    offerTitle: String,
    couponCode: String,
    daysRemaining: String,
    onApplyClick: () -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFBF190E), Color(0xFFEB001B))
    )

    Row(
        Modifier
            .padding(vertical = 10.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .size(80.dp)
                .background(brush = gradientBrush),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$discountPercentage%",
                fontSize = 34.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = offerTitle, fontWeight = FontWeight.Bold)
                Text(text = couponCode, fontSize = 14.sp)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = daysRemaining, color = Color(0xFF9B9B9B), fontSize = 12.sp
                )
                Button(
                    onClick = onApplyClick, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDB3022), contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.apply), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
