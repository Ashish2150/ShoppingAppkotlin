package com.example.shoppingapp

import android.app.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    val isEditable: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoopingList() {
    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }
    var showAlertDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showAlertDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItem) { item ->
                if (item.isEditable) {
                    ShoppingEditorItem(item = item, onEditComplete = {
                        editName, editQuantity ->
                        sItem = sItem.map { it.copy(isEditable = false) }
                        val editItem = sItem.find { it.id == item.id }
                        editItem?.let {
                            it.name = editName
                            it.quantity = editQuantity
                        }
                    })
                } else {
                    ShoppingListItem(item = item, onEditClick = {
                        sItem = sItem.map { it.copy(isEditable = it.id == item.id) }
                    }, onDeleteClick = {
                        sItem = sItem - item
                    })
                }
            }

        }
    }

    if (showAlertDialog){
        AlertDialog(onDismissRequest = { showAlertDialog = false }, confirmButton = {
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        if(itemName.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = sItem.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItem = sItem + newItem
                            showAlertDialog = false
                            itemName = ""
                            itemQuantity = ""
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = {
                        showAlertDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
        },
            title = { Text("Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName, onValueChange = {
                        itemName = it
                    },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    OutlinedTextField(value = itemQuantity, onValueChange = {
                        itemQuantity = it
                    },
                        singleLine = true
                    )
                }
            })
    }
}

@Composable
fun ShoppingEditorItem(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editName by remember { mutableStateOf(item.name) }
    var editQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditable) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        Column {
            BasicTextField(
                value = editName,
                onValueChange = {editName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editQuantity,
                onValueChange = { editQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editName, editQuantity.toIntOrNull() ?: 1)
        }) {
            Text("Save")
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: ()-> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(percent = 20)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        Text("${item.name}",
            modifier = Modifier.padding(8.dp))
        Text("Qty: ${item.quantity}",
            modifier = Modifier.padding(8.dp))
        Row() {
            IconButton(onClick =  onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}


