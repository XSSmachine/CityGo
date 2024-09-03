package com.example.userrequest.create

import android.annotation.SuppressLint

import android.os.Build
import androidx.annotation.RequiresApi

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.userrequest.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateDetailsUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isFormValid by createUserRequestViewModel.isFormValid2.collectAsState()


    Scaffold(
        topBar = {
            ProgressTopAppBar(progress = 1, navigateUp = navigateUp)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 7.responsiveHeight())
        ) {
            addressDetails1(createUserRequestViewModel)
            Spacer(modifier = Modifier.height(1.responsiveHeight()))
            Divider()
            Spacer(modifier = Modifier.height(1.responsiveHeight()))
            addressDetails2(createUserRequestViewModel)
            Spacer(modifier = Modifier.height(1.responsiveHeight()))
            Divider()
            Spacer(modifier = Modifier.height(1.responsiveHeight()))


            val timeSlots = createUserRequestViewModel.generateTimeSlots()

            val todayTimeSlots = timeSlots.filter {
                it.contains(
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            }
            val tomorrowTimeSlots = timeSlots.filter {
                it.contains(
                    LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            }

            val selectedChipIndex = remember { mutableStateOf<Int?>(null) }
            val selectedGroup = remember { mutableStateOf<String?>(null) }

            Column {
                Row {
                    Spacer(modifier = Modifier.width(3.responsiveWidth()))
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = "Clock icon"
                    )
                    Spacer(modifier = Modifier.width(1.responsiveWidth()))
                    Text(
                        text = stringResource(id = R.string.pickup_time),
                        style = TextStyle.Default.copy(fontSize = 20.sp) // Set font size to 20sp
                    )
                }
                ElevatedCard(
                    modifier = Modifier
                        .padding(horizontal = 7.responsiveHeight())
                        .padding(top = 2.responsiveHeight(), bottom = 3.responsiveHeight())
                ) {
                    Text(
                        stringResource(id = R.string.today_title),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(1.responsiveHeight())
                    )
                    ChipVerticalGrid(
                        spacing = 1.responsiveHeight(),
                        modifier = Modifier.padding(1.responsiveHeight())
                    ) {
                        todayTimeSlots.forEachIndexed { index, word ->
                            val isSelected =
                                selectedGroup.value == "today" && index == selectedChipIndex.value
                            val customIconToggleButtonColors =
                                IconButtonDefaults.iconToggleButtonColors(
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
                                    .padding(
                                        vertical = 1.responsiveHeight(),
                                        horizontal = 2.responsiveWidth()
                                    )
                                    .size(8.responsiveHeight(), 8.responsiveWidth()),
                                colors = customIconToggleButtonColors

                            ) {
                                Text(text = word.substringBefore(","))
                            }
                        }
                    }

                    Text(
                        stringResource(id = R.string.tommorow_title),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(1.responsiveHeight())
                    )
                    ChipVerticalGrid(
                        spacing = 1.responsiveHeight(),
                        modifier = Modifier.padding(1.responsiveHeight())
                    ) {
                        tomorrowTimeSlots.forEachIndexed { index, word ->
                            val isSelected =
                                selectedGroup.value == "tomorrow" && index == selectedChipIndex.value
                            val customIconToggleButtonColors =
                                IconButtonDefaults.iconToggleButtonColors(
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
                                    .padding(
                                        vertical = 1.responsiveHeight(),
                                        horizontal = 2.responsiveWidth()
                                    )
                                    .size(8.responsiveHeight(), 8.responsiveWidth()),
                                colors = customIconToggleButtonColors
                            ) {
                                Text(text = word.substringBefore(","))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(2.responsiveHeight()))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val userIdResult = createUserRequestViewModel.getUserId()
                        if (userIdResult.isSuccess) {
                            createUserRequestViewModel.saveAddresses()
                            navController.navigate("price")
                        } else {
                            navController.navigate("login")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.responsiveHeight()), // Make the button fill the maximum width
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow, // Set background color to yellow
                    contentColor = Color.Black // Set text color to black
                ),
                enabled = isFormValid

            ) {
                Text(
                    text = stringResource(id = R.string.btn_next_text),
                    style = TextStyle.Default.copy(fontSize = 20.sp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addressDetails1(createUserRequestViewModel: CreateUserRequestViewModel) {
    val lift = stringResource(id = R.string.lift)
    val stairs = stringResource(id = R.string.stairs)
    val items = remember {
        listOf(lift, stairs)
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(2.responsiveHeight())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_rocket_launch_24),
                contentDescription = "Job done icon"
            )
            Spacer(modifier = Modifier.width(1.responsiveWidth()))
            Text(
                text = stringResource(id = R.string.pickup_address),
                style = TextStyle.Default.copy(fontSize = 18.sp) // Set font size to 20sp
            )
        }


        Spacer(modifier = Modifier.height(1.responsiveHeight()))

        OutlinedTextField(
            value = createUserRequestViewModel.addressName1.value,
            onValueChange = { value ->
                createUserRequestViewModel.setAddressName1(value)
            },
            placeholder = { Text(stringResource(id = R.string.address_hint)) },
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(2.responsiveHeight()))
        Row(horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(id = R.string.lift_question))
            Spacer(modifier = Modifier.width(40.responsiveWidth()))
            Text(text = stringResource(id = R.string.floor))
        }
        Spacer(modifier = Modifier.height(1.responsiveHeight()))
        Row {


            TextSwitch(
                selectedIndex = createUserRequestViewModel.liftStairs1.value,
                items = items,
                onSelectionChange = { createUserRequestViewModel.setLiftStairs1(it) })
            Spacer(modifier = Modifier.width(10.responsiveWidth()))
            val pattern = remember { Regex("^\\d*\$") }
            TextField(
                modifier = Modifier
                    .width(23.responsiveWidth())
                    .height(7.responsiveHeight())
                    .padding(start = 2.responsiveHeight(), end = 2.responsiveHeight())
                    .background(Color.White, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.floors1.value,
                onValueChange = { value: String ->
                    if (value.matches(pattern)) {
                        createUserRequestViewModel.setFloors1(value)
                    }
                }, colors = TextFieldDefaults.textFieldColors(
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

        }
        Spacer(modifier = Modifier.height(2.responsiveHeight()))
        Row(horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(id = R.string.door_title))
            Spacer(modifier = Modifier.width(5.responsiveHeight()))
            Text(text = stringResource(id = R.string.number_title))
        }

        Spacer(modifier = Modifier.height(1.responsiveHeight()))
        Row {

            TextField(
                modifier = Modifier
                    .width(22.responsiveWidth())
                    .height(7.responsiveHeight())
                    .padding(top = 1.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.doorCode1.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setDoorCode1(value)
                },
                placeholder = { Text(text = "1234", textAlign = TextAlign.Center) },
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
            Spacer(modifier = Modifier.width(22.responsiveWidth()))

            TextField(
                modifier = Modifier
                    .width(53.responsiveWidth())
                    .height(7.responsiveHeight())
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.phoneNumber1.value,
                onValueChange = { value ->
                    if (value.length <= 15) { //  15 digits
                        createUserRequestViewModel.setPhoneNumber1(value)
                    }
                },
                prefix = {
                    if (createUserRequestViewModel.phoneNumber1.value.isNotEmpty()) Text(
                        text = "+385",
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.number_hint),
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
fun addressDetails2(createUserRequestViewModel: CreateUserRequestViewModel) {
    val lift = stringResource(id = R.string.lift)
    val stairs = stringResource(id = R.string.stairs)
    val items = remember {
        listOf(lift, stairs)
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(2.responsiveHeight())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_rocket_launch_24),
                contentDescription = "Job done icon"
            )
            Spacer(modifier = Modifier.width(1.responsiveWidth()))
            Text(
                text = stringResource(id = R.string.delivery_address),
                style = TextStyle.Default.copy(fontSize = 18.sp) // Set font size to 20sp
            )
        }


        Spacer(modifier = Modifier.height(1.responsiveHeight()))

        OutlinedTextField(
            value = createUserRequestViewModel.addressName2.value,
            onValueChange = { value ->
                createUserRequestViewModel.setAddressName2(value)
            },
            placeholder = { Text(stringResource(id = R.string.address_hint)) },
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(2.responsiveHeight()))
        Row(horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(id = R.string.lift_question))
            Spacer(modifier = Modifier.width(40.responsiveWidth()))
            Text(text = stringResource(id = R.string.floor))
        }
        Spacer(modifier = Modifier.height(1.responsiveHeight()))
        Row {


            TextSwitch(
                selectedIndex = createUserRequestViewModel.liftStairs2.value,
                items = items,
                onSelectionChange = { createUserRequestViewModel.setLiftStairs2(it) })
            Spacer(modifier = Modifier.width(10.responsiveWidth()))
            val pattern = remember { Regex("^\\d*\$") }
            TextField(
                modifier = Modifier
                    .width(23.responsiveWidth())
                    .height(7.responsiveHeight())
                    .padding(start = 2.responsiveHeight(), end = 2.responsiveHeight())
                    .background(Color.White, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.floors2.value,
                onValueChange = { value: String ->
                    if (value.matches(pattern)) {
                        createUserRequestViewModel.setFloors2(value)
                    }
                }, colors = TextFieldDefaults.textFieldColors(
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

        }
        Spacer(modifier = Modifier.height(2.responsiveHeight()))
        Row(horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(id = R.string.door_title))
            Spacer(modifier = Modifier.width(5.responsiveHeight()))
            Text(text = stringResource(id = R.string.number_title))
        }

        Spacer(modifier = Modifier.height(1.responsiveHeight()))
        Row {

            TextField(
                modifier = Modifier
                    .width(22.responsiveWidth())
                    .height(7.responsiveHeight())
                    .padding(top = 1.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = createUserRequestViewModel.doorCode2.value,
                onValueChange = { value ->
                    createUserRequestViewModel.setDoorCode2(value)
                },
                placeholder = { Text(text = "1234", textAlign = TextAlign.Center) },
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
            Spacer(modifier = Modifier.width(22.responsiveWidth()))

            TextField(
                modifier = Modifier
                    .width(53.responsiveWidth())
                    .height(7.responsiveHeight())
                    .background(Color.LightGray, RoundedCornerShape(5.dp)),
                value = createUserRequestViewModel.phoneNumber2.value,
                onValueChange = { value ->
                    if (value.length <= 15) { //  15 digits
                        createUserRequestViewModel.setPhoneNumber2(value)
                    }
                },
                prefix = {
                    if (createUserRequestViewModel.phoneNumber2.value.isNotEmpty()) Text(
                        text = "+385",
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.number_hint),
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                currentOrigin =
                    currentOrigin.copy(x = 0, y = currentOrigin.y + placeable.height + spacingValue)
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
            .height(7.responsiveHeight())
            .width(50.responsiveWidth())
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xfff3f3f2))
            .padding(1.responsiveHeight())
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
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}



