package com.example.bazaaro.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.app.ui.components.AlertDialogView

@Composable
fun ProfileScreen(navController: NavHostController) {
    MainView()
}

@Composable
fun MainView() {

    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        AlertDialogView(
            onDismissRequestText = "Cancel",
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmationText = "Log out",
            onConfirmation = {
                openAlertDialog.value = false
            },
            dialogTitle = "Log Out",
            dialogText = "Are you sure you want to log out? You will need to sign in again to access your account.",
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {

        Text(
            text = "Profile",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF222222),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)

        )

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true,
                alignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Ahmad")
                Text(text = "a@gmail.com")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            // item 1
            TextButton(onClick = {}) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF838383))
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "Personal information",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // item 2
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.ic_language),
                    contentDescription = null,
                    tint = Color(0xFF838383)
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Language",
                        color = Color.Black,
                    )
                    Text(text = "English (US)", color = Color(0xFF838383))
                }

            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 3
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {}) {
                Icon(Icons.Outlined.DateRange, contentDescription = null, tint = Color(0xFF838383))
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "Order history", color = Color.Black, modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 4
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {}) {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = Color(0xFF838383)
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "My favorites", color = Color.Black, modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 5
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {}) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF838383))
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "My address", color = Color.Black, modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 6
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = null,
                    tint = Color(0xFFDC1010)
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "Log out", color = Color(0xFFDC1010), modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun MainViewPrev() {
    MainView()
}