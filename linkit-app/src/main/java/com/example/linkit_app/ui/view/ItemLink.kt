package com.example.linkit_app.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.theme.BlackOpacity
import com.example.linkit_app.ui.theme.Link
import com.example.linkit_app.ui.theme.LinkitTheme

@Composable
fun ItemLink() {
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
            Image(
                painter = painterResource(id = R.drawable.image_analysis),
                contentDescription = "Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .weight(0.3f)
                    .clip(RoundedCornerShape(8.dp))
                    .wrapContentHeight()
            )

            Column(modifier = Modifier
                .weight(0.7f)
                .padding(start = 8.dp)) {
                Text(
                    text = "Title",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body1,
                    maxLines = 3
                )
                Text(
                    text = "Description",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body2,
                    color = BlackOpacity,
                    maxLines = 4
                )
                Text(
                    text = "https://google.com",
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
    }
}

@Preview
@Composable
fun ItemLinkPreview() {
    LinkitTheme {
        ItemLink()
    }
}