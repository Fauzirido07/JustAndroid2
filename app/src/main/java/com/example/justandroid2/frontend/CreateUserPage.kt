package com.example.justandroid2.frontend

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.justandroid2.PreferencesManager
import com.example.justandroid2.data.RegisterData
import com.example.justandroid2.respon.LoginRespon
import com.example.justandroid2.service.RegisterService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserPage(navController: NavController, context: Context = LocalContext.current){
    val preferencesManager = remember { PreferencesManager(context = context) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Homepage") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(value = username, onValueChange = { newText ->
                username = newText
            }, label = { Text("Username") })
            OutlinedTextField(value = password, onValueChange = { newText ->
                password = newText
            }, label = { Text("Password") })
            OutlinedTextField(value = email, onValueChange = { newText ->
                email = newText
            }, label = { Text("Email") })
            ElevatedButton(modifier = Modifier.width(280.dp),colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue), onClick = {
                var baseUrl = "http://10.0.2.2:1337/api/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RegisterService::class.java)
                val call = retrofit.saveData(RegisterData(email.text, username.text, password.text))
                call.enqueue(object : Callback<LoginRespon> {
                    override fun onResponse(
                        call: Call<LoginRespon>,
                        response: Response<LoginRespon>
                    ) {
                        print(response.code())
                        if (response.code() == 200) {
                            navController.navigate("Homepage")

                        } else if (response.code() == 400) {
                            print("error login")
                            var toast = Toast.makeText(
                                context,
                                "Username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                        print(t.message)
                    }

                })
            }) {
                Text("Simpan")
            }
            ElevatedButton(modifier = Modifier.width(280.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
            ),onClick = {
                preferencesManager.saveData("jwt", "")
                navController.navigate("greeting")
            }) {
                Text("Logout")
            }
        }
    }
}