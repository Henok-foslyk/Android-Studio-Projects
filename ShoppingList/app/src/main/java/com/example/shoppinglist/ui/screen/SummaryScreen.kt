package com.example.shoppinglist.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.R

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    summaryViewModel: SummaryViewModel = viewModel(),
    onBackClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFCC))
    ) {
        TextButton(
            onClick = onBackClicked,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(stringResource(id = R.string.back))
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(id = R.string.food)}: ${summaryViewModel.FoodItems}",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${stringResource(id = R.string.book)}: ${summaryViewModel.BookItems}",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${stringResource(id = R.string.electronics)}: ${summaryViewModel.ElectronicItems}",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "${stringResource(id = R.string.other)}: ${
                    summaryViewModel.allItems
                            - summaryViewModel.FoodItems
                            - summaryViewModel.BookItems
                            - summaryViewModel.ElectronicItems
                }",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(
                    R.drawable.bracket
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Text(
                text = "${stringResource(id = R.string.all)}: ${summaryViewModel.allItems}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}