package cz.mendelu.petstore.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius

data class PlaceholderScreenContent(
    val icon: ImageVector? = null,
    val title: String? = null,
    val text: String? = null,
    val buttonText: String? = null,
    val onButtonClick: (() -> Unit)? = null,
)

@Composable
fun PlaceHolderScreen(
    content: PlaceholderScreenContent,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(basicMargin()),
        ) {
            if (content.icon != null) {
                Icon(
                    imageVector = content.icon,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }

            if (content.title != null) {
                Spacer(modifier = Modifier.height(basicMargin()))
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            if (content.text != null) {
                Spacer(modifier = Modifier.height(basicMargin()))
                Text(
                    text = content.text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val buttonText = content.buttonText
            val onButtonClick = content.onButtonClick
            if (buttonText != null && onButtonClick != null) {
                Spacer(modifier = Modifier.height(basicMargin()))
                Button(
                    onClick = onButtonClick,
                    shape = RoundedCornerShape(basicRadius())
                ) {
                    Text(text = buttonText)
                }
            }
        }
    }
}
