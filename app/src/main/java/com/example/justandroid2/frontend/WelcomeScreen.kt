package com.example.justandroid2.frontend

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.justandroid2.PreferencesManager

@Composable
fun WelcomeScreen(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome To Pick Me Up",
            style = TextStyle(fontSize = 20.sp,),
            fontWeight = FontWeight(600),
            color = Color.Black,

            )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 60.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CS - 082232497147",
            style = TextStyle(fontSize = 20.sp,),
            fontWeight = FontWeight(600),
            color = Color.Black,
        )
    }

Column (
    modifier = Modifier
        .fillMaxSize()
        .padding(top = 100.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
)
{
    Button(onClick = {
        if (preferencesManager.getData("job") == "Editor") {
            navController.navigate("homepageeditor")
        } else if (preferencesManager.getData("job") == "Perekrut") {
            navController.navigate("Homepage")
        }
    })
    {
        Text(
            text = "Start",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
            )
        )


    }
    }
}



