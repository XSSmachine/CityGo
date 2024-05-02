package com.example.userrequest.create

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.userrequest.R
import com.google.android.material.chip.Chip
import com.hfad.model.Address
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateDetailsUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel(),
    navigateUp:() ->Unit,
) {



    val context = LocalContext.current

    

    Scaffold(
        topBar = {
            TopAppBar (
                navigationIcon = { IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }},
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Progress(1)
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(top=40.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(text = "Photo",style = TextStyle.Default.copy(fontSize = 11.sp), color = Color.Gray)
                        Spacer(modifier = Modifier.width(125.dp))
                        Text(text = "Address",style = TextStyle.Default.copy(fontSize = 11.sp), color = Color.Gray)
                        Spacer(modifier = Modifier.width(102.dp))
                        Text(text = "Set a price",style = TextStyle.Default.copy(fontSize = 11.sp), color = Color.Gray)
                    }


                }
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 50.dp)) {
            addressDetails1(createUserRequestViewModel)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            addressDetails2(createUserRequestViewModel)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))


            val timeSlots = createUserRequestViewModel.generateTimeSlots()

// Separate time slots into 'today' and 'tomorrow'
            val todayTimeSlots = timeSlots.filter { it.contains(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) }
            val tomorrowTimeSlots = timeSlots.filter { it.contains(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) }

// Define a mutable state to track the selected chip and the group it belongs to
            val selectedChipIndex = remember { mutableStateOf<Int?>(null) }
            val selectedGroup = remember { mutableStateOf<String?>(null) }

            Column {
                Row {
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_access_time_24), contentDescription = "Clock icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pick-up time",
                        style = TextStyle.Default.copy(fontSize = 20.sp) // Set font size to 20sp
                    )
                }
                ElevatedCard (modifier = Modifier.padding(horizontal = 50.dp).padding(top = 10.dp, bottom = 15.dp)){
                    Text("Today", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(7.dp))
                    ChipVerticalGrid(
                        spacing = 7.dp,
                        modifier = Modifier.padding(7.dp)
                    ) {
                        todayTimeSlots.forEachIndexed { index, word ->
                            val isSelected = selectedGroup.value == "today" && index == selectedChipIndex.value
                            val customIconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(
                                containerColor = Color.LightGray, // Background color when unchecked
                                checkedContainerColor = Color.Yellow, // Background color when checked
                                contentColor = Color.Black // Color of the content (icon and text)
                                , checkedContentColor = Color.Black
                            )
                            FilledIconToggleButton(
                                checked = isSelected,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedChipIndex.value = index
                                        selectedGroup.value = "today"
                                        // Update _timeTable value in the viewmodel
                                        createUserRequestViewModel.setTimeTable(word)
                                    } else {
                                        selectedChipIndex.value = null
                                        selectedGroup.value = null
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 3.dp, horizontal = 13.dp)
                                    .size(65.dp, 35.dp)
                                , colors = customIconToggleButtonColors

                            ) {
                                Text(text = word.substringBefore(","))
                            }
                        }
                    }

                    Text("Tomorrow", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(7.dp))
                    ChipVerticalGrid(
                        spacing = 7.dp,
                        modifier = Modifier.padding(7.dp)
                    ) {
                        tomorrowTimeSlots.forEachIndexed { index, word ->
                            val isSelected = selectedGroup.value == "tomorrow" && index == selectedChipIndex.value
                            val customIconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(
                                containerColor = Color.LightGray, // Background color when unchecked
                                checkedContainerColor = Color.Yellow, // Background color when checked
                                contentColor = Color.Black // Color of the content (icon and text)
                                , checkedContentColor = Color.Black
                            )
                            FilledIconToggleButton(
                                checked = isSelected,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedChipIndex.value = index
                                        selectedGroup.value = "tomorrow"
                                        // Update _timeTable value in the viewmodel
                                        createUserRequestViewModel.setTimeTable(word)
                                    } else {
                                        selectedChipIndex.value = null
                                        selectedGroup.value = null
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 3.dp, horizontal = 13.dp)
                                    .size(65.dp, 35.dp),
                                colors = customIconToggleButtonColors
                            ) {
                                Text(text = word.substringBefore(","))
                            }
                        }
                    }
                }
                }





            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                runBlocking {
                    if(createUserRequestViewModel.getUserId().isSuccess){
                        createUserRequestViewModel.saveAddresses()
                        navController.navigate("price")
                    }else{
                        navController.navigate("login")
                    }

                    //Prebaci na iduci screen
                }
            },
                modifier = Modifier.fillMaxWidth().padding(8.dp), // Make the button fill the maximum width
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow, // Set background color to yellow
                    contentColor = Color.Black // Set text color to black
                )

            ) {
                Text(text = "Next step",
                    style = TextStyle.Default.copy(fontSize = 20.sp))
            }

        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addressDetails1(createUserRequestViewModel: CreateUserRequestViewModel){
    val items = remember {
        listOf("Lift", "Stairs")
    }
    Column(horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Row {
            Icon(painter = painterResource(id = R.drawable.baseline_rocket_launch_24), contentDescription = "Job done icon")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Delivery address",
                style = TextStyle.Default.copy(fontSize = 20.sp) // Set font size to 20sp
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = createUserRequestViewModel.addressName1.value,
            onValueChange = { value ->
                createUserRequestViewModel.setAddressName1(value) },
            placeholder = { Text("Traži adresu") },
            modifier=Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                errorCursorColor = Color.Red,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                containerColor = Color.LightGray,
            ),

            )

        Spacer(modifier = Modifier.height(10.dp))
        Row (horizontalArrangement = Arrangement.Start){
            Text(text = "Fit in the lift?")
            Spacer(modifier = Modifier.width(180.dp))
            Text(text = "Floors")
        }

        Row{


            TextSwitch(selectedIndex = createUserRequestViewModel.liftStairs1.value, items = items, onSelectionChange = {createUserRequestViewModel.setLiftStairs1(it)})
            Spacer(modifier = Modifier.width(16.dp))
            val pattern = remember { Regex("^\\d*\$") }
            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.floors1.value,
                onValueChange = { value:String ->
                    if (value.matches(pattern)) {
                        createUserRequestViewModel.setFloors1(value)
                    }
                },colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

        }

        Row (horizontalArrangement = Arrangement.Start){
            Text(text = "Door code (optional)")
            Spacer(modifier = Modifier.width(50.dp))
            Text(text = "Contact phone number")
        }


        Row{

            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 5.dp, top = 1.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.doorCode1.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setDoorCode1(value)
                },
                placeholder = { Text(text = "1234",textAlign = TextAlign.Center)},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(70.dp))

            TextField(
                modifier = Modifier
                    .width(220.dp)
                    .height(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.phoneNumber1.value,
                onValueChange = { value ->
                    if (value.length <= 15) { //  15 digits
                        createUserRequestViewModel.setPhoneNumber1(value)
                    }
                },
                prefix = {if (createUserRequestViewModel.phoneNumber1.value.isNotEmpty()) Text(text = "+385", color = Color.Black,textAlign = TextAlign.Center)},
                placeholder = { Text(text = "Phone number or name",textAlign = TextAlign.Center)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                singleLine = true
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addressDetails2(createUserRequestViewModel: CreateUserRequestViewModel){
    val items = remember {
        listOf("Lift", "Stairs")
    }
    Column(horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Row {
            Icon(painter = painterResource(id = R.drawable.baseline_done_all_24), contentDescription = "Job done icon")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Delivery address",
                style = TextStyle.Default.copy(fontSize = 20.sp) // Set font size to 20sp
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = createUserRequestViewModel.addressName2.value,
            onValueChange = { value ->
                createUserRequestViewModel.setAddressName2(value) },
            placeholder = { Text("Traži adresu") },
            modifier=Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                errorCursorColor = Color.Red,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                containerColor = Color.LightGray,
            ),

        )

        Spacer(modifier = Modifier.height(10.dp))
        Row (horizontalArrangement = Arrangement.Start){
            Text(text = "Fit in the lift?")
            Spacer(modifier = Modifier.width(180.dp))
            Text(text = "Floors")
        }

        Row{


            TextSwitch(selectedIndex = createUserRequestViewModel.liftStairs2.value, items = items, onSelectionChange = {createUserRequestViewModel.setLiftStairs2(it)})
            Spacer(modifier = Modifier.width(16.dp))
            val pattern = remember { Regex("^\\d*\$") }
            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 15.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.floors2.value,
                onValueChange = { value:String ->
                    if (value.matches(pattern)) {
                        createUserRequestViewModel.setFloors2(value)
                    }
                },colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

        }

        Row (horizontalArrangement = Arrangement.Start){
            Text(text = "Door code (optional)")
            Spacer(modifier = Modifier.width(50.dp))
            Text(text = "Contact phone number")
        }


        Row{

            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(start = 5.dp, top = 1.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.doorCode2.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setDoorCode2(value)
                },
                placeholder = { Text(text = "1234",textAlign = TextAlign.Center)},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(70.dp))

            TextField(
                modifier = Modifier
                    .width(220.dp)
                    .height(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.phoneNumber2.value,
                onValueChange = { value ->
                    if (value.length <= 15) { //  15 digits
                        createUserRequestViewModel.setPhoneNumber2(value)
                    }
                },
                prefix = {if (createUserRequestViewModel.phoneNumber2.value.isNotEmpty()) Text(text = "+385", color = Color.Black,textAlign = TextAlign.Center)},
                placeholder = { Text(text = "Phone number or name",textAlign = TextAlign.Center)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    containerColor = Color.LightGray,
                ),
                singleLine = true
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

fun ContentDrawScope.drawWithLayer(block: ContentDrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}

@Composable
 fun TextSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<String>,
    onSelectionChange: (Int) -> Unit
) {

    BoxWithConstraints(
        modifier
            .padding(8.dp)
            .height(56.dp)
            .width(224.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xfff3f3f2))
            .padding(8.dp)
    ) {
        if (items.isNotEmpty()) {

            val maxWidth = this.maxWidth
            val tabWidth = maxWidth / items.size

            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                label = "indicator offset"
            )

            // This is for shadow layer matching white background
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .width(tabWidth)
                    .fillMaxHeight()
            )

            Row(modifier = Modifier
                .fillMaxWidth()

                .drawWithContent {

                    // This is for setting black tex while drawing on white background
                    val padding = 8.dp.toPx()
                    drawRoundRect(
                        topLeft = Offset(x = indicatorOffset.toPx() + padding, padding),
                        size = Size(size.width / 2 - padding * 2, size.height - padding * 2),
                        color = Color.Black,
                        cornerRadius = CornerRadius(x = 8.dp.toPx(), y = 8.dp.toPx()),
                    )

                    drawWithLayer {
                        drawContent()

                        // This is white top rounded rectangle
                        drawRoundRect(
                            topLeft = Offset(x = indicatorOffset.toPx(), 0f),
                            size = Size(size.width / 2, size.height),
                            color = Color.Yellow,
                            cornerRadius = CornerRadius(x = 8.dp.toPx(), y = 8.dp.toPx()),
                            blendMode = BlendMode.SrcOut
                        )
                    }

                }
            ) {
                items.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    onSelectionChange(index)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}




