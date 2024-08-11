package com.example.userrequest.create

import android.annotation.SuppressLint
import android.graphics.Color.toArgb
import android.graphics.Paint
import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.userrequest.R

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePriceUserRequestScreen(
    navController: NavController,
    createUserRequestViewModel: CreateUserRequestViewModel = hiltViewModel(),
    navigateUp:() ->Unit,
) {

    val context = LocalContext.current

    fun Int.add5(): Int {
        return this + 5
    }
    fun Int.minus5(): Int {
        return this - 5
    }


    var selectedSize by remember { mutableStateOf<String?>(null) }



    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Progress(2)
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 4.dp),
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
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(text = "Which size for your stuff?",style = TextStyle.Default.copy(fontSize = 25.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Normal), color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            Row {


                SizeSelector(
                    selectedSize = selectedSize,//createUserRequestViewModel.category,
                    onSizeSelected = { size ->
                        selectedSize=size//createUserRequestViewModel.setCategory(size)
                    })
            }
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp)
            ) {


                IconButton(onClick = { Log.d("Clicked",createUserRequestViewModel.price.toString())
                    createUserRequestViewModel.setPrice(createUserRequestViewModel.price.add5()
                    )
                    createUserRequestViewModel.incrementClickCounter()
                                     },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(50.dp)) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add +5 on the price")

                }

                IconButton(onClick = { if (createUserRequestViewModel.price>=5)createUserRequestViewModel.setPrice(createUserRequestViewModel.price.minus5())
                                     createUserRequestViewModel.decrementClickCounter()
                                     },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(50.dp)) {
                    Icon(painter = painterResource(id = R.drawable.baseline_minimize_24), contentDescription = "Take -5 on the price")

                }

                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = if(createUserRequestViewModel.price>=0) abs(createUserRequestViewModel.price).toString()+" €" else "0 €",
                    style = TextStyle.Default.copy(fontSize = 30.sp)
                )
                Content(createUserRequestViewModel)

            }
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            TextSwitch(selectedIndex = createUserRequestViewModel.extraWorker, items =
                listOf( R.drawable.person_running_solid_1,R.drawable.union)
            , onSelectionChange = {createUserRequestViewModel.setExtraWorker(it)})
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                createUserRequestViewModel.setCategory(selectedSize!!)
                createUserRequestViewModel.createContact()
                navController.navigate("home")
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .align(Alignment.End), // Make the button fill the maximum width
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow, // Set background color to yellow
                    contentColor = Color.Black // Set text color to black
                )

            ) {
                Text(text = "Submit",
                    style = TextStyle.Default.copy(fontSize = 20.sp))
            }
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
     // List of pairs representing (size, imageResource)
    selectedSize: String?,
    onSizeSelected: (String) -> Unit
) {
    val sizes = listOf(
        Triple(Color(0xFFE0D604),"S" , listOf(R.drawable.icon_electricity,R.drawable.icon_coffee_maker,R.drawable.icon_microwave,R.drawable.icon_sockets,R.drawable.icon_table)) ,
        Triple(Color(0xFF2A89AC),"M"  , listOf(R.drawable.icon_stove,R.drawable.icon_bbq,R.drawable.icon_television)) ,
        Triple(Color(0xFF549826),"L"  , listOf(R.drawable.icon_closet, R.drawable.icon_sofa,R.drawable.icon_refrigerator ))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        sizes.forEach { (color,size,imageResource) ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(interactionSource = interactionSource,
                        indication = null) {
                        onSizeSelected(size)

                    }
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(4.dp, if (selectedSize == size) Color.DarkGray else Color.Transparent),
                colors = CardDefaults.cardColors ( color),
                elevation = CardDefaults.cardElevation(4.dp),

            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val shuffledIcons = imageResource.shuffled()
                    val placedPositions = mutableListOf<Pair<Float, Float>>()
                    shuffledIcons.forEach { image ->
                        val (randomX, randomY) = generateRandomPosition(placedPositions)
                        placedPositions.add(randomX to randomY)
                        Image(
                            painter = painterResource(id = image), // Assuming imageResource is resource ID
                            contentDescription = "Size Image",
                            modifier = Modifier
                                .size(35.dp)
                                .offset(randomX.dp, randomY.dp)
                        )
                    }


//                    Image(
//                        painter = painterResource(id = imageResource.get(1)), // Assuming imageResource is resource ID
//                        contentDescription = "Size Image",
//                        modifier = Modifier.size(100.dp)
//                    )
//                    Image(
//                        painter = painterResource(id = imageResource.get(2)), // Assuming imageResource is resource ID
//                        contentDescription = "Size Image",
//                        modifier = Modifier.size(100.dp)
//                    )
                    Text(
                        text = size,
                        style = TextStyle(fontSize = 35.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(8.dp),
                        color=Color.White
                    )
                }
            }
        }
    }
}



private fun generateRandomPosition(placedPositions: List<Pair<Float, Float>>): Pair<Float, Float> {
    val maxX = 100f // Adjust as per your requirement
    val maxY = 155f // Adjust as per your requirement
    val minDistance = 35f // Minimum distance between images

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
fun Content(createUserRequestViewModel:CreateUserRequestViewModel) {
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

        drawCircle(color = Color(0xFFF7DC00).copy(alpha = 0.60f), style = Stroke(180f), radius = radius)
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