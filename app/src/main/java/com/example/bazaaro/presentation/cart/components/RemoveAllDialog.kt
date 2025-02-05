package com.example.bazaaro.presentation.cart.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bazaaro.R
import com.example.bazaaro.app.ui.components.AlertDialogView

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
