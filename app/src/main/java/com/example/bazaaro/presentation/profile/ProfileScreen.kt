package com.example.bazaaro.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.app.ui.components.AlertDialogView
import com.example.bazaaro.data.model.Language

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(), navController: NavHostController
) {

    val openAlertDialog = remember { mutableStateOf(false) }
    LogOutDialog(openAlertDialog)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {

        Text(
            text = stringResource(R.string.profile),
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
            TextButton(onClick = {
                navController.navigate("personal-information")
            }) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF838383))
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = stringResource(R.string.personalInformation),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 2
            var expanded by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                expanded = !expanded
            }) {
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
                        text = stringResource(R.string.language),
                        color = Color.Black,
                    )

                    Box {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = !expanded },
                        ) {
                            Language.entries.forEach { language ->
                                DropdownMenuItem(onClick = {
                                    profileViewModel.changeLanguage(language.code)
                                    expanded = false
                                }, text = {
                                    Text(
                                        text = language.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF838383),
                                    )
                                })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // item 3
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("order")
            }) {
                Icon(Icons.Outlined.DateRange, contentDescription = null, tint = Color(0xFF838383))
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = stringResource(R.string.orderHistory),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 4
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("favorite")
            }) {
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
                    text = stringResource(R.string.myFavorites),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // item 5
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("address")
            }) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF838383))
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = stringResource(R.string.myAddress),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
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
                    text = stringResource(R.string.logOut),
                    color = Color(0xFFDC1010),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LogOutDialog(openAlertDialog: MutableState<Boolean>) {
    if (openAlertDialog.value) {
        AlertDialogView(
            onDismissRequestText = stringResource(R.string.cancel),
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmationText = stringResource(R.string.logOut),
            onConfirmation = {
                openAlertDialog.value = false
            },
            dialogTitle = stringResource(R.string.logOut),
            dialogText = stringResource(R.string.logOutDialogText),
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout)
        )
    }
}