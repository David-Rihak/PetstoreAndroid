package cz.mendelu.petstore.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import cz.mendelu.petstore.R
import cz.mendelu.petstore.models.Pet
import androidx.compose.runtime.getValue
import androidx.compose.ui.autofill.contentType
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.cardColor
import cz.mendelu.petstore.ui.theme.fullCardElevation
import cz.mendelu.petstore.ui.theme.halfMargin

@Composable
fun ListRow(
    pet: Pet,
    onClick: () -> Unit
) {
    val petPhoto = pet.photoUrls?.firstOrNull()
    Card(
        modifier = Modifier
            .clickable(onClick = { onClick() })
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(basicRadius()),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
    ) {

        Row(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = halfMargin(), vertical = basicMargin()),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(basicRadius())
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.clip(RoundedCornerShape(basicRadius())).size(60.dp),
                    model = petPhoto,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = stringResource(R.string.pet_image),
                    loading = {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .size(60.dp)
                                .background(color = Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.HideImage, contentDescription = stringResource(R.string.petNoImage))
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    pet.name?.let { Text(it, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) }
                    pet.category?.let { Text("${it.name}", style = TextStyle(color = Color.Gray)) }
                }
            }

            Icon(Icons.Default.ChevronRight, contentDescription = stringResource(R.string.list_goToDetail), modifier = Modifier.size(32.dp))
        }
    }
}