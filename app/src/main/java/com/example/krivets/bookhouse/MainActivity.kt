package com.example.krivets.bookhouse


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController


import com.example.krivets.bookhouse.ui.theme.BookHouseTheme
import com.example.krivets.bookhouse.widgets.NavigationPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

var darkMode by mutableStateOf(false)
class MainActivity : ComponentActivity() {

    companion object {
        val names = listOf(
            R.string.faq,
            R.string.google,
            R.string.privacy,
            R.string.support,
            )

        val descriptions = listOf(
            R.string.faq_description,
            R.string.google_description,
            R.string.privacy_description,
            R.string.support_description,
        )

        val dayThemeImage = listOf(
            R.drawable.icon_111,
            R.drawable.icon_222,
            R.drawable.icon_333,
            R.drawable.icon_444,
        )


        val nightThemeImage = listOf(
            R.drawable.icon_11,
            R.drawable.icon_22,
            R.drawable.icon_33,
            R.drawable.icon_44,
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BookHouseTheme(darkTheme = darkMode){
               NavigationPage()

            }
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(drawerState: DrawerState, coroutineScope: CoroutineScope, title: String) {
    var borderColor = MaterialTheme.colorScheme.onPrimary
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = {

                if(drawerState.isClosed){
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }else{
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }

            }) {
                Icon(

                    imageVector = Icons.Rounded.List,
                    contentDescription = "icon",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color = MaterialTheme.colorScheme.onSurface)
                        .padding(5.dp)
                )
            }

        },

        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.background),

        modifier = Modifier
            .padding(2.dp)
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(size.width / 6f, size.height),
                    end = Offset(5 * size.width / 6f, size.height),
                    strokeWidth = 0.3.sp.toPx()
                )
            }
    )
}

@Composable
@Preview
fun Preview()
{
    BookHouseTheme(darkTheme = darkMode){
        NavigationPage()

    }
}



