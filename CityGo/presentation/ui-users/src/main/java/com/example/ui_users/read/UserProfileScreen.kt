package com.example.ui_users.read

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ui_users.R
import com.example.ui_users.login.UserLoginViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    userViewModel: UserProfileViewModel = hiltViewModel()
) {
//    Scaffold(
//        modifier = Modifier.semantics {
//            testTagsAsResourceId = true
//        },
//        containerColor = Color.Transparent,
//        contentColor = MaterialTheme.colorScheme.onBackground,
//        topBar = {
//            TopAppBar(
//                title = {
//
//                },
//                actions = {
//                    IconButton(onClick = { }) {
//                        Icon(Icons.Default.Face, contentDescription = "Search")
//                    }
//                    IconButton(onClick = { }) {
//                        Icon(Icons.Default.AccountBox, contentDescription = "More")
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = Color.Transparent,
//                ),
//            )
//        }
//    ) { padding ->
//        @Composable
//        fun ProfileContent(
//            modifier: Modifier = Modifier,
//            content: @Composable () -> Unit
//        ) {
//            Column(modifier) {
//                content()
//            }
//        }
//
//    }
//
//
//
//
//    @OptIn(ExperimentalLayoutApi::class)
//    @Composable
//    fun TopProfileLayout() {
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            shape = RoundedCornerShape(8),
//        ) {
//            Column(modifier = Modifier.padding(10.dp)) {
//                Row(
//                    modifier = Modifier.padding(vertical = 5.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//
//                    Column(
//                        modifier = Modifier
//                            .padding(horizontal = 8.dp)
//                            .weight(1f)
//                    ) {
//                        Text(
//                            text = "random text",
//                            style = MaterialTheme.typography.titleLarge
//                        )
//
//                        Text(
//                            text = userViewModel.name,
//                                    style = MaterialTheme . typography . labelMedium,
//                            overflow = TextOverflow.Ellipsis,
//                        )
//                    }
//                }
//
//                Text(
//                    modifier = Modifier.padding(vertical = 5.dp),
//                    text = userViewModel.email,
//                    style = MaterialTheme.typography.bodySmall,
//                )
//
//
//            }
//
//        }
//    }
//
//    @Composable
//    fun ImageTextContent(
//        icon: @Composable () -> Unit,
//        text: @Composable () -> Unit,
//        modifier: Modifier = Modifier
//    ) {
//        Row(
//            modifier,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            icon()
//            Spacer(modifier = Modifier.width(5.dp))
//            text()
//            Spacer(modifier = Modifier.width(10.dp))
//        }
//    }
//
//
////    @Composable
////    fun FooterContent() {
////        Surface(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(12.dp),
////            shape = RoundedCornerShape(8),
////        ) {
////            Column(modifier = Modifier.padding(5.dp)) {
////                Text(
////                    modifier = Modifier
////                        .padding(10.dp),
////                    text = stringResource(id = R.string.txt_more_options),
////                    style = MaterialTheme.typography.titleMedium,
////                )
////                moreOptionsList.forEach {
////                    MoreOptionsComp(it)
////                }
////            }
////        }
////    }
////
////    @Composable
////    fun MoreOptionsComp(
////        featureList: FeatureList,
////    ) {
////        Row(
////            modifier = Modifier.padding(5.dp),
////            verticalAlignment = Alignment.CenterVertically,
////        ) {
////            when (featureList.listIcon) {
////                is ImageVectorIcon -> Icon(
////                    imageVector = featureList.listIcon.imageVector,
////                    contentDescription = null,
////                    modifier = Modifier
////                        .size(40.dp)
////                        .padding(6.dp)
////                )
////
////                is DrawableResourceIcon -> Icon(
////                    painter = painterResource(id = featureList.listIcon.id),
////                    contentDescription = null,
////                    modifier = Modifier
////                        .size(40.dp)
////                        .padding(6.dp)
////                )
////            }
////            Column(
////                modifier = Modifier
////                    .padding(horizontal = 4.dp)
////                    .weight(1f)
////            ) {
////                Text(
////                    text = featureList.name,
////                    style = MaterialTheme.typography.labelLarge
////                )
////            }
////            Icon(
////                imageVector = Icons.Default.KeyboardArrowRight,
////                contentDescription = null,
////                modifier = Modifier.padding(4.dp)
////            )
////        }
////    }

}

    @Preview(showBackground = true)
    @Composable
    fun PreviewProfileScreen() {

    }