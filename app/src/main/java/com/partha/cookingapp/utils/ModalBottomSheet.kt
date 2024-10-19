package com.partha.cookingapp.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: (selectedTime: String) -> Unit = {},
    onCookNowClick: () -> Unit = {},
    selectedPeriod: String = "AM",
    onDismissRequest: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedTime by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        ModalBottomSheet(
            modifier = modifier
                .padding(innerPadding)
                .widthIn(max = 400.dp),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            content = {
                ScheduleCookingTimeContent(
                    onDeleteClick = onDeleteClick,
                    onRescheduleClick = { onRescheduleClick(selectedTime) },
                    onCookNowClick = onCookNowClick,
                    selectedPeriod = selectedPeriod,
                    onCloseClick = {
                        coroutineScope.launch { sheetState.hide() }
                        onDismissRequest()
                    },
                    onTimeSelected = { selectedTime1 ->
                        selectedTime =  selectedTime1
                    }
                )
            }
        )
    }

}


@Composable
fun ScheduleCookingTimeContent(
    onDeleteClick: () -> Unit = {},
    onRescheduleClick: () -> Unit = {},
    onCookNowClick: () -> Unit = {},
    selectedPeriod: String = "AM",
    onCloseClick: () -> Unit = {},
    onTimeSelected: (String) -> Unit = {}
) {
    var selectedHour by remember { mutableIntStateOf(6) }
    var selectedMinute by remember { mutableIntStateOf(30) }
    var selectedPeriodState by remember { mutableStateOf(selectedPeriod) }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScheduleCookingTimeHeader(onCloseClick = onCloseClick)

        ScheduleCookingTimeSelector(
            initialSelectedPeriod = selectedPeriodState,
            onPeriodChange = {
                selectedPeriodState = it
                onTimeSelected("$selectedHour:${String.format("%02d", selectedMinute)} $selectedPeriodState")
            },
            onHourChange = { hour ->
                selectedHour = hour-1
                onTimeSelected("$selectedHour:${String.format("%02d", selectedMinute)} $selectedPeriodState")
            },
            onMinuteChange = { minute ->
                selectedMinute = minute-1
                onTimeSelected("$selectedHour:${String.format("%02d", selectedMinute)} $selectedPeriodState")
            }
        )

        ScheduleCookingTimeActions(
            onDeleteClick = onDeleteClick,
            onRescheduleClick = {
                onRescheduleClick()
                onCloseClick()
            },
            onCookNowClick = onCookNowClick
        )
    }
}

@Composable
fun ScheduleCookingTimeHeader(onCloseClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Schedule cooking time",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF3B4E9B)
        )
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .border(1.dp, shape = CircleShape, color = DarkBlue)
                .size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = DarkBlue
            )
        }
    }
}

@Composable
fun ScheduleCookingTimeSelector(
    initialSelectedPeriod: String = "AM",
    onPeriodChange: (String) -> Unit = {},
    onHourChange: (Int) -> Unit = {},
    onMinuteChange: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        TimePicker(
            onHourChange = onHourChange,
            onMinuteChange = onMinuteChange
        )

        AMPMToggle(
            modifier = Modifier,
            initialSelected = initialSelectedPeriod,
            onSelectionChange = onPeriodChange
        )
    }
}


@Composable
fun TimePicker(
    onHourChange: (Int) -> Unit = {},
    onMinuteChange: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = Color(0xfff6f5fa),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        InfiniteCircularList(
            width = 95.dp,
            itemHeight = 35.dp,
            numberOfDisplayedItems = 3,
            items = (1..12).toList(),
            initialItem = 6,
            itemScaleFact = 1.5f,
            textStyle = TextStyle(fontSize = 20.sp),
            textColor = Color(0x993B4E9B),
            selectedTextColor = DarkBlue,
            onItemSelected = { index, item ->
                onHourChange(item)
            }
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.titleLarge,
            color = DarkBlue
        )
        InfiniteCircularList(
            width = 95.dp,
            itemHeight = 40.dp,
            numberOfDisplayedItems = 3,
            items = (0..59).toList(),
            initialItem = 30,
            itemScaleFact = 1.5f,
            textStyle = TextStyle(fontSize = 20.sp),
            textColor = Color(0x993B4E9B),
            selectedTextColor = DarkBlue,
            onItemSelected = { index, item ->
                onMinuteChange(item)
            }
        )
    }
}

@Composable
fun ScheduleCookingTimeActions(
    onDeleteClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    onCookNowClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
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


@Composable
fun AMPMToggle(
    modifier: Modifier = Modifier,
    initialSelected: String = "AM",
    onSelectionChange: (String) -> Unit = {}
) {
    var selected by remember { mutableStateOf(initialSelected) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToggleButton(
            label = "AM",
            isSelected = selected == "AM",
            onClick = {
                selected = "AM"
                onSelectionChange("AM")
            }
        )

        ToggleButton(
            label = "PM",
            isSelected = selected == "PM",
            onClick = {
                selected = "PM"
                onSelectionChange("PM")
            }
        )
    }
}

@Composable
fun ToggleButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF3B4E9B) else Color(0xFFe6e6fe),
            contentColor = if (isSelected) Color.White else DarkBlue
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewScheduleCookingTimeContent() {
    ScheduleCookingTimeContent()
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleCookingTimeBottomSheet() {
    ScheduleCookingTimeBottomSheet()
}