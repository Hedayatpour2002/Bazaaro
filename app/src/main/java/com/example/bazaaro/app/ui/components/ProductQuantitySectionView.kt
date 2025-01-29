package com.example.bazaaro.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R


@Composable
fun ProductQuantitySectionView(
    quantity: Int, onIncrement: () -> Unit, onDecrement: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onDecrement() }, modifier = Modifier.size(32.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_minus),
                contentDescription = null,
                tint = if (quantity == 1) Color(0xFFDB3022) else Color(0xFF838383)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        AnimatedContent(
            targetState = quantity, transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                } else {
                    slideInVertically { height -> -height } + fadeIn() togetherWith slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }, label = "animated content"
        ) { targetCount ->
            Text(text = "$targetCount", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ic_add))
        var isPlaying by remember { mutableStateOf(false) }

        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            speed = 5f,
        )

        LaunchedEffect(progress) {
            if (progress == 1f) {
                isPlaying = false
            }
        }

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .clickable {
                    isPlaying = true
                    onIncrement()
                }
                .size(48.dp),
        )
    }
}