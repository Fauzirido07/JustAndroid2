package com.example.justandroid2.frontend

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditorDetailScreen(
    navController: NavController,
    id: String?,
    username: String?,
    alamat: String?,
    status: String?,
    job: String?,
    context: Context = LocalContext.current
) {
    val preferencesManager = remember { PreferencesManager(context = context) }

    var username by remember { mutableStateOf(username ?: "") }
    var job by remember { mutableStateOf(job ?: "") }
    var status by remember { mutableStateOf(status ?: "") }
    var alamat by remember { mutableStateOf(alamat ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("Homepage") }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                        Text(text = "Pick Me Up")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
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
                    text = username, style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    )
                )
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                Text(
                    text = status + " - " + job,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.64.sp,
                        color = Color(0x801E1E1E),
                    )
                )
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    if (status == "tetap") {
                        Button(
                            onClick = { navController.navigate("buatjadwal/" + id) },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .weight(1f)
                        ) {
                            Text(text = "Rekrut Editor")
                        }

                    } else {
                        Button(
                            onClick = { navController.navigate("buatjadwal/" + id) },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .weight(1f),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.Red,
//                        ),
                        ) {
                            Text(text = "Sewa Editor")
                        }
                    }


                }


            }
        }
    }
}