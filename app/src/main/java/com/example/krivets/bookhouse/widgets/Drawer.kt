package com.example.krivets.bookhouse.widgets

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.krivets.bookhouse.ui.navigation.AboutPage
import com.example.krivets.bookhouse.ui.navigation.HomePage
import com.example.krivets.bookhouse.R
import com.example.krivets.bookhouse.darkMode
import com.example.krivets.bookhouse.ui.navigation.BooksPage
import com.example.krivets.bookhouse.ui.navigation.EditPage
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawerItems(navController: NavHostController, drawerState: DrawerState) {


    var scope = rememberCoroutineScope()
    var currentBackStackEntryAsState = navController.currentBackStackEntryAsState()
    var destination = currentBackStackEntryAsState.value?.destination

    navigationItems.forEach { item ->
        NavigationDrawerItem(
            icon = { Icon(item.icon, contentDescription = item.title) },
            label = { Text(text = item.title) },
            selected = destination?.route == item.route,
            onClick = {
                navController.navigate(item.route, navOptions {
                    this.launchSingleTop = true
                    this.restoreState = true
                })
                scope.launch {

                    drawerState.close()
                }

            }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun NavigationPage() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetNavigator = rememberBottomSheetNavigator()

    val navController = rememberNavController(bottomSheetNavigator)
    val slideIn = slideInHorizontally(
        initialOffsetX = {fullWidth -> fullWidth},
        animationSpec = tween(delayMillis = 40)
    )


    ModalNavigationDrawer(drawerContent = { DrawerContent(navController,drawerState) }, drawerState = drawerState) {



        ModalBottomSheetLayout(bottomSheetNavigator,  modifier = Modifier.fillMaxSize()) {

            NavHost(navController = navController, startDestination = "HomePage",  modifier = Modifier.fillMaxSize()) {
                composable("HomePage", enterTransition = { slideIn }, exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(delayMillis = 50)
                    )
                })
                {
                    HomePage(drawerState, coroutineScope, navController)

                }
                composable("BooksPage", enterTransition = { slideIn }, exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(delayMillis = 50)
                    )
                })
                {
                    BooksPage(drawerState, coroutineScope)

                }
                composable("AboutPage", enterTransition = { slideIn }, exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(delayMillis = 50)
                    )
                })
                {
                    AboutPage(drawerState, coroutineScope)

                }

                bottomSheet(route = "EditPage/{id}") { backStackEntry ->
                    val idString = backStackEntry.arguments?.getString("id")
                    EditPage(id = idString, navController = navController, coroutineScope) // Добавляем модификатор fillMaxSize()
                }




            }
        }



    }

}

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState) {

    var themeImage = R.drawable.day_image
    if(darkMode) themeImage = R.drawable.night_image
    var borderColor = MaterialTheme.colorScheme.onPrimary

    var imageSize = 260.dp
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            imageSize = 180.dp
        }
    }

    ModalDrawerSheet {
        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .drawBehind {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height), //(0,0) at top-left point of the box
                        end = Offset(size.width, size.height),//bottom-left point of the box
                        strokeWidth = 0.1.sp.toPx()
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Box()
            {
                Image(
                    painter = painterResource(id = themeImage),
                    contentDescription = "theme_image",
                    modifier = Modifier
                        .size(imageSize)

                )

            }
            Box()
            {
                Switch(checked = darkMode, onCheckedChange = { darkMode =!darkMode } )
            }
        }

        NavigationDrawerItems(navController,drawerState)
    }

}

data class NavigationItem(var icon: ImageVector, var title: String, var route: String)

var navigationItems = listOf(
    NavigationItem(Icons.Filled.Home,"Home", "HomePage"),
    NavigationItem(Icons.Filled.Book,"Books", "BooksPage"),
    NavigationItem(Icons.Filled.Info,"About", "AboutPage"),
)


