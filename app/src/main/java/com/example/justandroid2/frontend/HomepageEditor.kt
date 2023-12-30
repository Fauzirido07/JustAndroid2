package com.example.justandroid2.frontend

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.justandroid2.PreferencesManager
import com.example.justandroid2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageEditor(navController: NavController, context: Context = LocalContext.current){
    //var listUser: List<UserRespon> = remember
    val preferencesManager = remember { PreferencesManager(context = context) }


    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                preferencesManager.clearData()
                navController.navigate("greeting")
            }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "logout")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Pick Me Up - Editor") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },) {
            innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 42.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Image(
                    modifier = Modifier
                        .width(114.dp)
                        .height(114.dp),
                    painter = painterResource(id = R.drawable.editor),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.padding(top = 14.dp, bottom = 14.dp))
                Text(
                    text = preferencesManager.getData("username"), style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    )
                )
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                Text(
                    text = preferencesManager.getData("status") + " - " + preferencesManager.getData("job"),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.64.sp,
                        color = Color(0x801E1E1E),
                    ))
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                Button(onClick = { navController.navigate("editProfile/" + preferencesManager.getData("iduser" ) + "/" + preferencesManager.getData("username" ) + "/" + preferencesManager.getData("job" ) + "/" + preferencesManager.getData("email" ) + "/" + preferencesManager.getData("alamat" ) + "/" + preferencesManager.getData("birth" ) + "/" + preferencesManager.getData("status" ))
                 },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 90.dp, end = 90.dp)
                    ) {
                    Text(text = "Edit Profile")
                }
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f)
                    ) {
                        Text(text = "Terima Tawaran")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                        ),
                    ) {
                        Text(text = "Tolak Tawaran")
                    }
                }


            }
        }
    }
}