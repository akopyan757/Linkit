package com.example.linkit_app.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.link.LinkItem
import com.example.linkit_app.ui.theme.BlackOpacity
import com.example.linkit_app.ui.theme.Link
import com.example.linkit_app.ui.theme.LinkitTheme
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun ItemLinkLayout(item: LinkItem, checked: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp, 8.dp, 4.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .defaultMinSize(minHeight = 50.dp)
        ) {

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .clip(RoundedCornerShape(8.dp))
                    .wrapContentHeight()
            ) {

                Image(
                    painter = rememberCoilPainter(
                        request = item.photoUrl,
                        previewPlaceholder = R.drawable.image_analysis
                    ),
                    contentDescription = "Image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                if (item.isPlayer) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_player_button),
                        contentDescription = "Player",
                        modifier = Modifier.width(32.dp).height(32.dp).align(Alignment.Center)
                    )
                }
            }

            Column(modifier = Modifier
                .weight(0.7f)
                .padding(start = 8.dp)) {
                Text(
                    text = item.title,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body1,
                    maxLines = 3
                )
                if (item.description.isNotEmpty()) {
                    Text(
                        text = item.description,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.body2,
                        color = BlackOpacity,
                        maxLines = 4
                    )
                }
                Text(
                    text = item.site,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body2,
                    color = Link,
                    maxLines = 4
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_baseline_share_24),
                contentDescription = "Share",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)

            )
        }

        if (checked) {
            Image(
                painter = painterResource(id = R.drawable.ic_link_checked),
                contentDescription = "Selected",
                modifier = Modifier
                    .padding(4.dp)
                    .wrapContentWidth(Alignment.Start)
            )
        }
    }
}

@Preview
@Composable
fun ItemLinkPreview() {
    LinkitTheme {
        ItemLinkLayout(
            LinkItem(
                title = "Title",
                description = "Description",
                site = "Youtube",
                url = "https://google.com/",
                photoUrl = "https://i.ytimg.com/vi/YadRW3eM2sg/maxresdefault.jpg",
                isPlayer = true
            ),
            checked = true
        )
    }
}