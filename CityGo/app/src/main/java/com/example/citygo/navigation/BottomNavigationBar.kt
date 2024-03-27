package com.example.citygo.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    val bottomScreens = listOf(
        NavigationItem.Home,
        NavigationItem.RequestList,
        NavigationItem.Profile,
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        // .alpha(0.95F)
        // .padding(horizontal = 16.dp, vertical = 8.dp)


    ) {

        // BottomNavigation(backgroundColor = MaterialTheme.colors.surface,modifier= Modifier.padding(horizontal = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            bottomScreens.map {
                val isSelected = navHostController
                    .currentBackStackEntryAsState().value?.destination?.route == it.route
                val animatedWeight by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
                CustomBottomNavItem(
                    screen = it, isSelected = isSelected,
                    modifier = Modifier.weight(animatedWeight)
                        .clickable {
                            navHostController.navigate(
                                it.route
                            ) {
                                restoreState = true
                                launchSingleTop = true

                                val destination = navHostController.graph.findStartDestination().id
                                popUpTo(destination) { saveState = true }
                            }
                        },
                )
                // Text(text = it.title)

                /* BottomNavigationItem(
                     selected = isSelected,
                    onlick = {
                         navHostController.navigate(
                             it.route
                         ) {
                             restoreState = true
                             launchSingleTop = true

                             val destination = navHostController.graph.findStartDestination().id
                             popUpTo(destination) { saveState = true }
                         }
                     },
                     icon = {
                         Icon(painter = painterResource(id = it.icon), contentDescription = "bottom Bar Icon", modifier = Modifier.size(24.dp))
                     },
                     label = {
                         Text(text = it.title,)
                     },

                     unselectedContentColor = Color.Gray,
                     selectedContentColor = MaterialTheme.colors.primary
                 )*/
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun CustomBottomNavItem(
    modifier: Modifier = Modifier,
    screen: NavigationItem,
    isSelected: Boolean,
) {
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected) 30.dp else 25.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(if (isSelected) 36.dp else 26.dp)
                .shadow(
                    elevation = if (isSelected) 15.dp else 0.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(20.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(screen.icon,"ikona")

            if (isSelected) {
                Text(
                    text = screen.title,
                    modifier = Modifier.padding(start = 8.dp, end = 10.dp),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}