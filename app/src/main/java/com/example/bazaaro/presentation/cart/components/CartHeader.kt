package com.example.bazaaro.presentation.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bazaaro.R

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