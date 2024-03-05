package com.example.userrequest.create

import android.annotation.SuppressLint
import android.widget.ToggleButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.chip.Chip
import com.hfad.model.Address
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateDetailsUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel()
) {



    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar (
                title = {
                    Text(text = "CYGO")
                }
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 50.dp)) {
            addressDetails1(createUserRequestViewModel)
            Spacer(modifier = Modifier.width(16.dp))
            addressDetails2(createUserRequestViewModel)
            Spacer(modifier = Modifier.width(16.dp))


            val timeSlots = generateTimeSlots()

// Define a mutable state to track the selected chip
            val selectedChipIndex = remember { mutableStateOf<Int?>(null) }
            ChipVerticalGrid(
                spacing = 7.dp,
                modifier = Modifier
                    .padding(7.dp)
            ) {
                timeSlots.forEachIndexed { index, word ->
                    val isSelected = index == selectedChipIndex.value

                    FilledIconToggleButton(
                        checked = isSelected,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedChipIndex.value = index
                                // Update _timeTable value in the viewmodel
                                createUserRequestViewModel.setTimeTable(word)
                            } else {
                                selectedChipIndex.value = null
                            }
                        },
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 13.dp).size(65.dp,35.dp)
                    ) {
                        Text(text = word)
                    }
                }
            }


            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                runBlocking {
                    createUserRequestViewModel.saveAddresses()
                    navController.navigate("price")
                    //Prebaci na iduci screen
                }
            }) {
                Text(text = "Idući korak")
            }

        }


    }
}

@Composable
fun addressDetails1(createUserRequestViewModel: CreateUserRequestViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

        Spacer(modifier = Modifier.width(16.dp))
        Text("Početna adresa")

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedTextField(
            value = createUserRequestViewModel.addressName1.value,
            onValueChange = { value ->
                createUserRequestViewModel.setAddressName1(value) },
            label = { Text("Traži adresu") }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Row{

            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.floors1.value.toString(),
                onValueChange = { value ->
                    createUserRequestViewModel.setFloors1(value.toInt())
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium
            )

        }

        Text("Uslikaj i odradi transport već danas!")

        Spacer(modifier = Modifier.width(16.dp))

        Row{

            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.doorCode1.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setDoorCode1(value)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.phoneNumber1.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setPhoneNumber1(value)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodyMedium
            )


        }
    }
}


@Composable
fun addressDetails2(createUserRequestViewModel: CreateUserRequestViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Text("Dostavna adresa")

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedTextField(
            value = createUserRequestViewModel.addressName2.value,
            onValueChange = { value ->
                createUserRequestViewModel.setAddressName2(value) },
            label = { Text("Traži adresu") }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Row{

            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.floors2.value.toString(),
                onValueChange = { value ->
                    createUserRequestViewModel.setFloors2(value.toInt())
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium
            )

        }

        Text("Uslikaj i odradi transport već danas!")

        Spacer(modifier = Modifier.width(16.dp))

        Row{

            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.doorCode2.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setDoorCode2(value)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.phoneNumber2.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setPhoneNumber2(value)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodyMedium
            )


        }
    }
}


@Composable
fun ChipVerticalGrid(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var currentRow = 0
        var currentOrigin = IntOffset.Zero
        val spacingValue = spacing.toPx().toInt()
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentOrigin.x > 0f && currentOrigin.x + placeable.width > constraints.maxWidth) {
                currentRow += 1
                currentOrigin = currentOrigin.copy(x = 0, y = currentOrigin.y + placeable.height + spacingValue)
            }

            placeable to currentOrigin.also {
                currentOrigin = it.copy(x = it.x + placeable.width + spacingValue)
            }
        }

        layout(
            width = constraints.maxWidth,
            height = placeables.lastOrNull()?.run { first.height + second.y } ?: 0
        ) {
            placeables.forEach {
                val (placeable, origin) = it
                placeable.place(origin.x, origin.y)
            }
        }
    }
}

fun generateTimeSlots(): List<String> {
    val timeSlots = mutableListOf<String>()
    val sdf = SimpleDateFormat("HH", Locale.getDefault())
    val currentTime = Date()
    val calendar = Calendar.getInstance()

    calendar.time = currentTime
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    val startTime = currentHour + 1
    val endTime = currentHour + 24 + (24-currentHour) // Generating time slots for the next 24 hours

    for (i in startTime until endTime) {
        val slotStartHour = i % 24 // Convert to 24-hour format
        val slotEndHour = (i + 1) % 24 // Convert to 24-hour format
        val slot = "${slotStartHour.toString().padStart(2, '0')} - ${slotEndHour.toString().padStart(2, '0')}"
        timeSlots.add(slot)
    }

    return timeSlots
}


