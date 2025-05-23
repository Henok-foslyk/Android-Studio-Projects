package com.example.weatherwiz.ui.screen


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherwiz.data.CityItem
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAPISelected: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var showCityDialogue by rememberSaveable { mutableStateOf(false) }
    var cityToEdit: CityItem? = null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cities",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.clearAllCities()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete all"
                        )
                    }
                    IconButton(
                        onClick = {
                            cityToEdit = null
                            showCityDialogue = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add"
                        )
                    }

                }
            )
        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (showCityDialogue) {
                cityDialog(
                    viewModel = viewModel,
                    cityToEdit = cityToEdit,
                    onCancel = {
                        showCityDialogue = false
                    }
                )
            }

            if (viewModel.getAllCityList().isEmpty()) {
                Text("Please Add a City")
            } else {
                LazyColumn {
                    items(
                        items =viewModel.getAllCityList(),
                        key = { cityItem -> cityItem.id}
                    ) { cityItem ->
                        CityCard(
                            cityItem = cityItem,
                            onDelete = {
                                viewModel.removeCityItem(cityItem)
                            },
                            onEdit = { cityForEdit ->
                                cityToEdit = cityForEdit
                                showCityDialogue= true
                            },
                            onPress = {
                                onAPISelected(cityItem.name)
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun cityDialog(
    viewModel: MainViewModel,
    cityToEdit: CityItem? = null,
    onCancel: () -> Unit
) {
    var cityName by remember {
        mutableStateOf(
            cityToEdit?.name ?: ""
        )
    }

    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    if (cityToEdit == null) "New City" else "Edit City",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("City name") },
                    value = cityName,
                    onValueChange = { cityName = it })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (cityToEdit == null) {
                            viewModel.addCityList(
                                CityItem(
                                    name = cityName,
                                    addDate = Date(System.currentTimeMillis()).toString(),
                                )
                            )
                        } else {
                            val editedCity = cityToEdit.copy(
                                name = cityName,
                                addDate = Date(System.currentTimeMillis()).toString(),
                            )
                            viewModel.editCityItem(cityToEdit, editedCity)
                        }
                        onCancel()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun CityCard(
    cityItem: CityItem,
    onDelete: () -> Unit,
    onEdit: (CityItem) -> Unit,
    onPress: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        onClick = {onPress()},
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = cityItem.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .clickable { onDelete() }
                            .padding(end = 8.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = "Edit",
                        modifier = Modifier.clickable { onEdit(cityItem) },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = "Added on -- ${cityItem.addDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}