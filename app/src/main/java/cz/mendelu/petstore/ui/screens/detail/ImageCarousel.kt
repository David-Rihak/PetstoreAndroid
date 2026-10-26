package cz.mendelu.petstore.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import cz.mendelu.petstore.R
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.halfMargin
import cz.mendelu.petstore.ui.theme.largeRadius

@Composable
fun ImageCarousel(
    imageUrls: List<String>,
    onRemoveImageClick: (Int) -> Unit,
    onAddImageClick: () -> Unit
){
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(halfMargin()),
        contentPadding = PaddingValues(horizontal = basicMargin(), vertical = halfMargin())
    ) {
        itemsIndexed(imageUrls) { index, imageUrl ->
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(largeRadius()))
                    .background(Color.LightGray)
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.pet_image),
                    loading = {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(basicRadius()))
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.HideImage, contentDescription = stringResource(R.string.petNoImage), modifier=Modifier.size(50.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(halfMargin())
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onRemoveImageClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.remove_image),
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }


        }

        item {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(largeRadius()))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onAddImageClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_image),
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}