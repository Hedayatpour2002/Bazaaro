package com.example.bazaaro.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.bazaaro.app.ui.components.ProductQuantitySectionView
import com.example.bazaaro.data.model.CartEntity

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
