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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.data.model.Coupon


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
                    Text(
                        text = stringResource(R.string.apply),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
