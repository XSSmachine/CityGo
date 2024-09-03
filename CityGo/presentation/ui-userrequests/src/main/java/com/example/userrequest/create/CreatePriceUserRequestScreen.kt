package com.example.userrequest.create

import android.annotation.SuppressLint

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.userrequest.R
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreatePriceUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {

    val context = LocalContext.current

    fun Int.add5(): Int {
        return this + 5
    }

    fun Int.minus5(): Int {
        return this - 5
    }

    val isFormValid by createUserRequestViewModel.isFormValid3.collectAsState()

    Scaffold(
        topBar = {
            ProgressTopAppBar(progress = 2, navigateUp = navigateUp)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.responsiveHeight())
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = stringResource(id = R.string.price_title),
                style = TextStyle.Default.copy(
                    fontSize = 25.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Normal
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.responsiveHeight()))
            Row {


                SizeSelector(
                    selectedSize = createUserRequestViewModel.category,
                    onSizeSelected = { size ->
                        createUserRequestViewModel.setCategory(size)
                    })
            }
            Spacer(modifier = Modifier.height(4.responsiveHeight()))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(40.responsiveHeight())
            ) {


                IconButton(
                    onClick = {
                        createUserRequestViewModel.setPrice(createUserRequestViewModel.price.add5())
                        createUserRequestViewModel.incrementClickCounter()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(4.responsiveHeight())
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add +5 on the price"
                    )

                }

                IconButton(
                    onClick = {
                        if (createUserRequestViewModel.price >= 5) createUserRequestViewModel.setPrice(
                            createUserRequestViewModel.price.minus5()
                        )
                        createUserRequestViewModel.decrementClickCounter()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(4.responsiveHeight())
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_minimize_24),
                        contentDescription = "Take -5 on the price"
                    )

                }

                Text(
                    modifier = Modifier
                        .padding(2.responsiveHeight()),
                    text = if (createUserRequestViewModel.price >= 0) abs(createUserRequestViewModel.price).toString() + " €" else "0 €",
                    style = TextStyle.Default.copy(fontSize = 30.sp)
                )
                Content(createUserRequestViewModel)

            }
            Spacer(modifier = Modifier.height(2.responsiveHeight()))

            Spacer(modifier = Modifier.height(2.responsiveHeight()))

            TextSwitch(selectedIndex = createUserRequestViewModel.extraWorker,
                items =
                listOf(R.drawable.person_running_solid_1, R.drawable.union),
                onSelectionChange = { createUserRequestViewModel.setExtraWorker(it) })
            Spacer(modifier = Modifier.height(2.responsiveHeight()))
            SubmitButton(
                createUserRequestViewModel = createUserRequestViewModel,
                navController = navController,
                isFormValid = isFormValid
            )

        }
    }
}

@Composable
fun TextSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<Int>,
    onSelectionChange: (Int) -> Unit
) {

    BoxWithConstraints(
        modifier
            .padding(1.responsiveHeight())
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
                        Icon(
                            painterResource(id = text),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


@Stable
@Composable
fun SizeSelector(
    selectedSize: String?,
    onSizeSelected: (String) -> Unit
) {
    val sizes = remember {
        listOf(
            Triple(
                Color(0xFFE0D604),
                "S",
                listOf(
                    R.drawable.icon_electricity,
                    R.drawable.icon_coffee_maker,
                    R.drawable.icon_microwave,
                    R.drawable.icon_sockets,
                    R.drawable.icon_table
                )
            ),
            Triple(
                Color(0xFF2A89AC),
                "M",
                listOf(R.drawable.icon_stove, R.drawable.icon_bbq, R.drawable.icon_television)
            ),
            Triple(
                Color(0xFF549826),
                "L",
                listOf(R.drawable.icon_closet, R.drawable.icon_sofa, R.drawable.icon_refrigerator)
            )
        )
    }

    val iconPositions = remember {
        sizes.associate { (_, size, imageResource) ->
            size to imageResource.map {
                mutableStateOf(generateRandomPosition(emptyList()))
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.responsiveHeight())
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        sizes.forEach { (color, size, imageResource) ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSizeSelected(size)
                        // Regenerate positions only for the selected card
                        iconPositions[size]?.forEachIndexed { index, position ->
                            position.value = generateRandomPosition(
                                iconPositions[size]?.take(index)?.map { it.value } ?: emptyList()
                            )
                        }
                    }
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    4.dp,
                    if (selectedSize == size) Color.DarkGray else Color.Transparent
                ),
                colors = CardDefaults.cardColors(color),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    imageResource.forEachIndexed { index, image ->
                        val position = iconPositions[size]?.get(index)?.value ?: Pair(0f, 0f)
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "Size Image",
                            modifier = Modifier
                                .size(4.responsiveHeight())
                                .offset(position.first.dp, position.second.dp)
                        )
                    }
                    Text(
                        text = size,
                        style = TextStyle(fontSize = 35.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun generateRandomPosition(placedPositions: List<Pair<Float, Float>>): Pair<Float, Float> {
    val maxX = 100f
    val maxY = 155f
    val minDistance = 35f

    var randomX = (0..maxX.toInt()).random().toFloat()
    var randomY = (50..maxY.toInt()).random().toFloat()

    while (placedPositions.any { (x, y) ->
            val distance = sqrt((randomX - x).pow(2) + (randomY - y).pow(2))
            distance < minDistance
        }) {
        randomX = (0..maxX.toInt()).random().toFloat()
        randomY = (50..maxY.toInt()).random().toFloat()
    }
    return randomX to randomY
}

@Composable
fun Content(createUserRequestViewModel: CreateUserRequestViewModel) {
    var radius by remember {
        mutableStateOf(0f)
    }

    var shapeCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var handleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var angle by remember {
        mutableStateOf(20.0)
    }

    var price by remember {
        mutableStateOf(0)
    }


    var previousAngle = 0.0

    Canvas(
        modifier = Modifier
            .size(250.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    handleCenter += dragAmount

                    angle = getRotationAngle(handleCenter, shapeCenter)
                    val deltaAngle = angle - previousAngle
                    if (abs(deltaAngle) >= 5.0) {
                        val direction = if (deltaAngle > 0) 1 else -1
                        if (price >= 0) {
                            price += direction
                        } else {
                            price = 0
                        }

                        createUserRequestViewModel.setPrice(price + createUserRequestViewModel.getClickCount() * 5)




                        previousAngle = angle
                    }
                    change.consume()
                }
            }
            .padding(10.dp)

    ) {
        shapeCenter = center

        radius = size.minDimension / 2

        val x = (shapeCenter.x + cos(Math.toRadians(angle)) * radius).toFloat()
        val y = (shapeCenter.y + sin(Math.toRadians(angle)) * radius).toFloat()

        handleCenter = Offset(x, y)

        val gradient = Brush.linearGradient(
            colors = listOf(
                Color.Yellow.copy(alpha = 0.1f),
                Color.Yellow.copy(alpha = 0.5f)
            ),
            start = Offset(0f, 0f),
            end = Offset(0f, 100f)
        )

        drawCircle(
            color = Color(0xFFF7DC00).copy(alpha = 0.60f),
            style = Stroke(180f),
            radius = radius
        )
        drawArc(
            color = Color.Yellow.copy(alpha = 0.09f),
            startAngle = 0f,
            sweepAngle = angle.toFloat(),
            useCenter = false,
            style = Stroke(180f),
            blendMode = BlendMode.Lighten

        )

        drawCircle(color = Color.Cyan.copy(alpha = 0.2f), center = handleCenter, radius = 150f)

        val arrowSize = 23f
        val arrowPath = Path().apply {
            moveTo(handleCenter.x, handleCenter.y - arrowSize)
            lineTo(handleCenter.x - arrowSize, handleCenter.y + arrowSize)
            lineTo(handleCenter.x + arrowSize, handleCenter.y + arrowSize)
            close()
        }
        drawPath(path = arrowPath, color = Color.DarkGray)

    }
}

private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
    val (dx, dy) = currentPosition - center
    val theta = atan2(dy, dx).toDouble()

    var angle = Math.toDegrees(theta)

    if (angle < 0) {
        angle += 360.0
    }
    return angle
}

@Composable
fun SubmitButton(
    createUserRequestViewModel: CreateUserRequestViewModel,
    navController: NavController,
    isFormValid:Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (!isLoading && isFormValid) {
                isLoading = true
                coroutineScope.launch {
                    try {
                        createUserRequestViewModel.createContact()
                        navController.navigate("home")
                    } catch (e: Exception) {
                        Log.e("SubmitButton", "Error creating contact", e)
                    } finally {
                        isLoading = false
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Yellow,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        ),
        enabled = isFormValid && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Black
            )
        } else {
            Text(
                text = stringResource(id = R.string.submit_btn),
                style = TextStyle.Default.copy(fontSize = 20.sp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
