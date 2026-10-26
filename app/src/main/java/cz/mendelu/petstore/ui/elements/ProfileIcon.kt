package cz.mendelu.petstore.ui.elements

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.petstore.R

@Composable
fun ProfileIcon(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(8.dp).size(32.dp).clip(RoundedCornerShape(100)).background(color = Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                onClick()
            }
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = stringResource(R.string.icon_profile),
                tint = Color.Gray,
                modifier = Modifier.size(20.dp))
        }
    }
}
