package com.example.justandroid2.frontend

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Editor(val name: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditorDetailScreen(editor: Editor, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Editor Detail") },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = editor.name)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = editor.description)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Lakukan sesuatu saat tombol "Rekrut" ditekan
                        //navController.navigate("rekrut/${editor.name}")
                    }
                ) {
                    Text(text = "Rekrut")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        // Lakukan sesuatu saat tombol "Sewa" ditekan
                        navController.navigate("sewa/${editor.name}")
                    }
                ) {
                    Text(text = "Sewa")
                }
            }
        }
    )
}