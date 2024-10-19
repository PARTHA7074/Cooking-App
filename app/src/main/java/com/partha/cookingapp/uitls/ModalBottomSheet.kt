package com.partha.cookingapp.uitls


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partha.cookingapp.ui.theme.DarkBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCookingTimeBottomSheet(
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: () -> Unit = {},
    onCookNowClick: () -> Unit = {},
    selectedTime: String = "06:30",
    selectedPeriod: String = "AM",
    onDismissRequest: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        ScheduleCookingTimeContent(
            onDeleteClick = onDeleteClick,
            onRescheduleClick = onRescheduleClick,
            onCookNowClick = onCookNowClick,
            selectedTime = selectedTime,
            selectedPeriod = selectedPeriod,
            onCloseClick = {
                coroutineScope.launch { sheetState.hide() }
                onDismissRequest()
            }
        )
    }
}


@Composable
fun ScheduleCookingTimeContent(
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: () -> Unit = {},
    onCookNowClick: () -> Unit = {},
    selectedTime: String = "06:30",
    selectedPeriod: String = "AM",
    onCloseClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Schedule cooking time",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF3B4E9B)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        // Time Selector (Placeholder)
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    InfiniteCircularList(
                        width = 80.dp,
                        itemHeight = 35.dp,
                        numberOfDisplayedItems = 3,
                        items = (1..12).toList(),
                        initialItem = 6,
                        itemScaleFact = 1.5f,
                        textStyle = TextStyle(fontSize = 20.sp),
                        textColor = DarkBlue,
                        selectedTextColor = DarkBlue,
                        onItemSelected = { index, item ->
                            // Handle item selection
                        }
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF3B4E9B)
                    )
                    InfiniteCircularList(
                        width = 80.dp,
                        itemHeight = 35.dp,
                        numberOfDisplayedItems = 3,
                        items = (1..60).toList(),
                        initialItem = 30,
                        itemScaleFact = 1.5f,
                        textStyle = TextStyle(fontSize = 20.sp),
                        textColor = DarkBlue,
                        selectedTextColor = DarkBlue,
                        onItemSelected = { index, item ->
                            // Handle item selection
                        }
                    )
                }

            }

            AMPMToggle(
                modifier = Modifier.padding(start = 10.dp),
                initialSelected = "AM",
                onSelectionChange = { selectedPeriod ->
                    // Handle the selection change
                }
            )
        }


        // Action Buttons
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Delete Button
            TextButton(onClick = onDeleteClick) {
                Text(
                    "Delete",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }


            // Reschedule Button
            OutlinedButton(
                modifier = Modifier.padding(start = 16.dp),
                onClick = onRescheduleClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFFFA726)
                ),
                border = BorderStroke(1.dp, Color(0xFFFFA726)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Re-schedule")
            }

            // Cook Now Button
            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = onCookNowClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cook Now", color = Color.White)
            }
        }
    }
}

@Composable
fun AMPMToggle(
    modifier: Modifier = Modifier,
    initialSelected: String = "AM",
    onSelectionChange: (String) -> Unit = {}
) {
    var selected by remember { mutableStateOf(initialSelected) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // AM Button
        TextButton(
            onClick = {
                selected = "AM"
                onSelectionChange("AM")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == "AM") Color(0xFF3B4E9B) else Color(0xFFEFEFEF),
                contentColor = if (selected == "AM") Color.White else Color.Gray
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("AM", fontWeight = if (selected == "AM") FontWeight.Bold else FontWeight.Normal)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // PM Button
        TextButton(
            onClick = {
                selected = "PM"
                onSelectionChange("PM")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == "PM") Color(0xFF3B4E9B) else Color(0xFFEFEFEF),
                contentColor = if (selected == "PM") Color.White else Color.Gray
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("PM", fontWeight = if (selected == "PM") FontWeight.Bold else FontWeight.Normal)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewScheduleCookingTimeContent() {
    ScheduleCookingTimeContent(
        onDeleteClick = {},
        onRescheduleClick = {},
        onCookNowClick = {},
        selectedTime = "06:30",
        selectedPeriod = "AM",
        onCloseClick = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewScheduleCookingTimeBottomSheet() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }) {
                    Text("Show Bottom Sheet")
                }
            }
        }
    )

    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        ScheduleCookingTimeContent(
            onDeleteClick = {},
            onRescheduleClick = {},
            onCookNowClick = {},
            onCloseClick = { coroutineScope.launch { sheetState.hide() } }
        )
    }
}






