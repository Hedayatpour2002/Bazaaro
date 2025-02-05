package com.example.bazaaro.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.app.ui.components.DashedDivider
import com.example.bazaaro.app.ui.components.ModalBottomSheetView
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.model.Coupon
import com.example.bazaaro.presentation.cart.CartViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSummaryView(cartViewModel: CartViewModel, products: List<CartEntity>) {
    val selectedCoupon by cartViewModel.selectedCoupon.collectAsStateWithLifecycle()

    val totalPrice = remember(products) {
        products.sumOf { it.price * it.quantity }
    }
    val totalPriceStr = "%.2f".format(totalPrice)


    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheetView(showBottomSheet = showBottomSheet, sheetState = sheetState) {
        CouponBottomSheetContent(cartViewModel = cartViewModel) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                cartViewModel.changeSelectedCoupon(
                    cartViewModel.couponCodeInput.value
                )
                if (!sheetState.isVisible) {
                    showBottomSheet.value = false
                }
            }
        }
    }

    val discountAmount = remember(selectedCoupon, totalPrice) {
        if (selectedCoupon != null) {
            (totalPrice * (selectedCoupon!!.discountPercentage.toDouble() / 100))
        } else {
            0.0
        }
    }
    val discountAmountStr = "%.2f".format(discountAmount)

    val finalTotal = remember(totalPrice, discountAmount) {
        totalPrice - discountAmount
    }
    val finalTotalStr = "%.2f".format(finalTotal)


    PaymentSummaryContent(selectedCoupon = selectedCoupon,
        totalPriceStr = totalPriceStr,
        discountAmountStr = discountAmountStr,
        finalTotalStr = finalTotalStr,
        removeSelectedCoupon = { cartViewModel.changeSelectedCoupon("") },
        onCouponClick = { showBottomSheet.value = !showBottomSheet.value },
        onCheckoutClick = { /* TODO: Handle Checkout */ })
}

@Composable
fun PaymentSummaryContent(
    selectedCoupon: Coupon?,
    totalPriceStr: String,
    discountAmountStr: String,
    finalTotalStr: String,
    removeSelectedCoupon: () -> Unit,
    onCouponClick: () -> Unit,
    onCheckoutClick: () -> Unit
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
        SummaryRow(
            label = stringResource(R.string.discount), value = "-$${discountAmountStr}"
        )

        DashedDivider()

        SummaryRow(
            label = stringResource(R.string.total_amount),
            value = "$${finalTotalStr}",
            isTotal = true
        )

        CouponButton(
            selectedCouponCode = selectedCoupon?.couponCode,
            removeSelectedCoupon = removeSelectedCoupon,
            onClick = onCouponClick
        )

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
fun CouponButton(
    selectedCouponCode: String?, removeSelectedCoupon: () -> Unit, onClick: () -> Unit
) {
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

                if (selectedCouponCode != null) {
                    Icon(Icons.Rounded.Delete,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            removeSelectedCoupon()
                        })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = selectedCouponCode, color = Color(0xFF838383), fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.do_you_have_coupon_code),
                        color = Color(0xFF838383),
                        fontSize = 12.sp
                    )
                }

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