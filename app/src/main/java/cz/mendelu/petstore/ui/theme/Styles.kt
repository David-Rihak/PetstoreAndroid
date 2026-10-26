package cz.mendelu.petstore.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val buttonStyle = ButtonColors(
    containerColor = primary,
    contentColor = text,
    disabledContainerColor = background,
    disabledContentColor = text
)

val titleStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp
)