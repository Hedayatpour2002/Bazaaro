package com.example.bazaaro.app.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetView(
    showBottomSheet: MutableState<Boolean>, sheetState: SheetState, content: @Composable () -> Unit
) {
    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            containerColor = Color(0xFFF9F9F9),
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            content()
        }
    }
}