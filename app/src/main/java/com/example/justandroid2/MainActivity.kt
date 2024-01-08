package com.example.justandroid2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.justandroid2.frontend.Homepage
import com.example.justandroid2.data.LoginData
import com.example.justandroid2.frontend.BuatJadwal
import com.example.justandroid2.frontend.CreateUserPage
import com.example.justandroid2.frontend.EditUserPage
import com.example.justandroid2.frontend.EditorDetailScreen
import com.example.justandroid2.frontend.HomepageEditor
import com.example.justandroid2.frontend.UploadCV
import com.example.justandroid2.respon.LoginRespon
import com.example.justandroid2.service.LoginService
import com.example.justandroid2.ui.theme.JustAndroid2Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAndroid2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val preferencesManager = remember { PreferencesManager(context = LocalContext.current) }
                    val sharedPreferences: SharedPreferences = LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    val navController = rememberNavController()

                    var startDestination: String
                    var jwt = sharedPreferences.getString("jwt", "")
                    var job = sharedPreferences.getString("job", "")
                    if(jwt.equals("")){
                        startDestination = "greeting"
                    }else{
                        if(job.equals("Editor")){
                            startDestination = "homepageeditor"
                        }else{
                            startDestination = "Homepage"
                        }
                    }

                    NavHost(navController, startDestination = startDestination) {
                        composable(route = "greeting") {
                            Login(navController)
                        }
                        composable(route = "Homepage") {
                            Homepage(navController)
                        }
                        composable(route = "CreateUserPage") {
                            CreateUserPage(navController)
                        }
                        composable(route = "homepageeditor") {
                            HomepageEditor(navController)
                        }
                        composable(route = "buatjadwal/{id}") { backStackEntry ->
                            BuatJadwal(navController, backStackEntry.arguments?.getString("id"))
                        }
                        composable(route = "uploadcv/{id}") { backStackEntry ->
                            UploadCV(navController, backStackEntry.arguments?.getString("id"))
                        }
                        composable(
                            route = "EditProfile/{userid}/{username}/{job}/{email}/{alamat}/{birth}/{status}/{profile}",
                        ) { backStackEntry ->

                            EditUserPage(
                                navController, backStackEntry.arguments?.getString("userid"),
                                backStackEntry.arguments?.getString("username"),
                                backStackEntry.arguments?.getString("job"),
                                backStackEntry.arguments?.getString("email"),
                                backStackEntry.arguments?.getString("alamat"),
                                backStackEntry.arguments?.getString("birth"),
                                backStackEntry.arguments?.getString("status"),
                                backStackEntry.arguments?.getString("profile"),
                            )

                        }
                        composable("detailEditor/{id}/{username}/{alamat}/{status}/{job}/{profile}") { backStackEntry ->
                            EditorDetailScreen(
                                navController,
                                backStackEntry.arguments?.getString("id"),
                                backStackEntry.arguments?.getString("username"),
                                backStackEntry.arguments?.getString("alamat"),
                                backStackEntry.arguments?.getString("status"),
                                backStackEntry.arguments?.getString("job"),
                                backStackEntry.arguments?.getString("profile"),
                            )
                        }
                        }
                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) } // Create PreferencesManager if it's not defined in your code.
    val baseUrl = "http://10.0.2.2:1337/api/"
    var jwt by remember { mutableStateOf("") }

    jwt = preferencesManager.getData("jwt")

    // Create mutable state variables for username and password
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.padding(PaddingValues(top = 130. dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Login", style = TextStyle(fontSize = 40.sp))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(value = username, onValueChange = { newText -> username = newText}, label = { Text("Username") })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(70.dp, 0.dp, 70.dp, 0.dp)) {
            Button(
                onClick = {
                    // Handle login button click
                    val retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(LoginService::class.java)
                    val call = retrofit.getData(LoginData(username.text, password.text))
                    call.enqueue(object : Callback<LoginRespon> {
                        override fun onResponse(call: Call<LoginRespon>, response: Response<LoginRespon>) {
                            if (response.code() == 200) {
                                val sahit = response.body()!!
                                jwt = response.body()?.jwt!!
                                preferencesManager.saveData("jwt", jwt)
                                preferencesManager.saveData("iduser", response.body()?.user?.id.toString())
                                preferencesManager.saveData("username", response.body()?.user?.username.toString())
                                preferencesManager.saveData("job", response.body()?.user?.job.toString())
                                preferencesManager.saveData("email", response.body()?.user?.email.toString())
                                preferencesManager.saveData("alamat", response.body()?.user?.alamat.toString())
                                preferencesManager.saveData("status", response.body()?.user?.status.toString())
                                preferencesManager.saveData("birth", response.body()?.user?.birth.toString())

                                if(response.body()?.user?.job.toString() == "Editor"){
                                    navController.navigate("homepageeditor")
                                }else if (response.body()?.user?.job.toString() == "Perekrut"){
                                    navController.navigate("Homepage")
                                }
                            } else if (response.code() == 400) {
                                Toast.makeText(context, "Username atau password salah", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                            print(t.message)
                        }
                    })
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
            ClickableText(
                text = AnnotatedString("Sign up here"),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(60.dp),
                onClick = {navController.navigate("CreateUserPage")} ,
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
