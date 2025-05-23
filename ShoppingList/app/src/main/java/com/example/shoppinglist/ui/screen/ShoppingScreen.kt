package com.example.shoppinglist.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.data.ShoppingItem

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.data.ShoppingCategory
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.text.any
import kotlin.text.isDigit
import kotlin.text.isNotBlank

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    viewModel: ShoppingViewModel = hiltViewModel(),
    onInfoClicked: (Int, Int, Int, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val shoppingList by viewModel.getAllItems().collectAsState(emptyList())

    var showShoppingDialogue by rememberSaveable { mutableStateOf(false) }

    var shoppingToEdit: ShoppingItem? by rememberSaveable {
        mutableStateOf(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.shoppingList))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF914d26),
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.clearAllItems()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.deleteAll)
                        )
                    }
                    IconButton(
                        onClick = {
                            shoppingToEdit = null
                            showShoppingDialogue = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = stringResource(id = R.string.add)
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val allItems = viewModel.getAllItemsNum()
                                val foods = viewModel.getFoodsNum()
                                val books = viewModel.getBooksNum()
                                val electronics = viewModel.getElectronicsNum()

                                onInfoClicked(
                                    allItems, foods, books, electronics
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.summary)
                        )
                    }
                }
            )
        }

    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .background(Color(0xFFFFFFCC))
            .fillMaxSize()
        ) {

            if (showShoppingDialogue) {
                ShoppingDialogue(
                    viewModel = viewModel,
                    itemToEdit = shoppingToEdit,
                    onCancel = {
                        showShoppingDialogue = false
                    }
                )
            }


            if (shoppingList.isEmpty()) {
                Text(stringResource(id = R.string.emptyList))
            } else {
                LazyColumn {
                    items(shoppingList) { shoppingItem ->

                        ShoppingCard(
                            shoppingItem = shoppingItem,
                            onCheckedChange = { checkBoxState ->
                                viewModel.changeShoppingState(shoppingItem, checkBoxState)
                            },
                            onDelete = {
                                viewModel.removeShoppingItem(shoppingItem)
                            },
                            onEdit = { shoppingItemToEdit ->
                                shoppingToEdit = shoppingItemToEdit
                                showShoppingDialogue = true
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ShoppingCard(
    shoppingItem: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: (ShoppingItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var itemChecked by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6fc972),
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = shoppingItem.isBought,
                    onCheckedChange = { onCheckedChange(it) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painter = painterResource(id = shoppingItem.category.getIcon()),
                    contentDescription = stringResource(id = R.string.priority),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )

                Text(shoppingItem.title, modifier = Modifier.fillMaxWidth(0.4f))
                Spacer(modifier = Modifier.fillMaxSize(0.5f))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete),
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .clickable {
                            onDelete()
                        },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = stringResource(id = R.string.edit),
                    modifier = Modifier.clickable {
                        onEdit(shoppingItem)
                    },
                    tint = Color.Blue
                )
                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.expandOrClose)
                    )
                }
            }

            if (expanded) {
                Text(
                    shoppingItem.description,
                    style = TextStyle(
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = "${stringResource(id = R.string.price)}: ${shoppingItem.price}",
                    style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ShoppingDialogue(
    viewModel: ShoppingViewModel,
    itemToEdit: ShoppingItem? = null,
    onCancel: () -> Unit
) {
    var itemTitle by remember {
        mutableStateOf(
            itemToEdit?.title ?: ""
        )
    }
    var itemDesc by remember {
        mutableStateOf(
            itemToEdit?.description ?: ""
        )
    }

    var itemPrice by remember {
        mutableStateOf(itemToEdit?.price?.toString() ?: 0.toString())
    }

    var priceError by remember {
        mutableStateOf(false)
    }

    var itemCategory by remember {
        mutableStateOf(itemToEdit?.category ?:ShoppingCategory.OTHER)
    }

    val isTitleValid = itemTitle.isNotBlank()
    val isPriceValid = !priceError

    val isSaveEnabled = isTitleValid && isPriceValid

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
                    if (itemToEdit == null)
                        stringResource(id = R.string.newItem)
                    else
                        stringResource(id = R.string.editItem),
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.itemTitle)) },
                    value = "$itemTitle",
                    onValueChange = { itemTitle = it })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.itemDescription)) },
                    value = "$itemDesc",
                    onValueChange = { itemDesc = it })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.itemPrice))},
                    value = "$itemPrice",
                    // Check for the conversion to int working properly
                    onValueChange = {
                        itemPrice = it
                        priceError = if (it.isNotBlank()) {
                            it.any { char -> !char.isDigit() }
                        } else {
                            false
                        }
                    },
                    isError = priceError,
                    supportingText = {
                        if (priceError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.errorPrompt),
                                color = Color.Red
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                CategoryDropdown(
                    selectedCategory = itemCategory,
                    onCategorySelected = { itemCategory = it }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            if (itemToEdit == null) {
                                viewModel.addShoppingList (
                                    ShoppingItem(
                                        id = 0,
                                        title = itemTitle,
                                        description = itemDesc,
                                        price = itemPrice.toInt(),
                                        category = itemCategory,
                                    )
                                )
                            } else {
                                val editedItem = itemToEdit.copy(
                                    title = itemTitle,
                                    description = itemDesc,
                                    category = itemCategory,
                                    price = itemPrice.toInt()

                                )
                                viewModel.editShoppingItem(editedItem)
                            }

                            onCancel()
                        },
                        enabled = isSaveEnabled
                    ) {
                        Text(stringResource(id = R.string.save))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: ShoppingCategory,
    onCategorySelected: (ShoppingCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = selectedCategory.toString(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.dropdown),
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            ShoppingCategory.values().forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category.toString()) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

