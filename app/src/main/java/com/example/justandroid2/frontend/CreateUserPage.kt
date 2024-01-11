package com.example.justandroid2.frontend

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.justandroid2.data.RegisterData
import com.example.justandroid2.respon.LoginRespon
import com.example.justandroid2.service.RegisterService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserPage(navController: NavController, context: Context = LocalContext.current){
    val primaryColor = Color(0xFF596FB7)
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var alamat by remember { mutableStateOf(TextFieldValue("")) }
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    var mDate by remember { mutableStateOf("Select Date") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val formattedMonth = String.format("%02d", mMonth + 1)
            val formattedDay = String.format("%02d", mDayOfMonth)
            mDate = "$mYear-$formattedMonth-$formattedDay"
        }, mYear, mMonth, mDay
    )

    Scaffold (
        topBar = {
                TopAppBar(
                    title = { Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("greeting") }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                        Text(text = "Register") }
                            },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = primaryColor,
                        titleContentColor = Color.White,
                    ),
                )
                 },) {
            innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(PaddingValues(top = 50.dp)),
                value = username,
                onValueChange = { newText ->
                    username = newText
                },
                label = { Text("Username") })
            OutlinedTextField(value = password, onValueChange = { newText ->
                password = newText
            }, label = { Text("Password") })
            OutlinedTextField(value = email, onValueChange = { newText ->
                email = newText
            }, label = { Text("Email") })
            OutlinedTextField(value = alamat, onValueChange = { newText ->
                alamat = newText
            }, label = { Text("Address") }, singleLine = false)

            val options = listOf("Editor", "Perekrut")
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }
            var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

            Column {
                OutlinedTextField(
                    value = selectedOptionText,
                    onValueChange = { selectedOptionText },
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                        },
                    label = { Text("Job") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "contentDescription",
                            modifier = Modifier.clickable {
                                expanded = !expanded
                            }
                        )
                    },
                    readOnly = true,
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current)
                        { mTextFieldSize.width.toDp() })
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            },
                            text =  { Text(selectionOption) }
                        )
                    }
                }
            }

            val options2 = listOf("Tetap", "Freelancer")
            var expanded2 by remember { mutableStateOf(false) }
            var selectedOptionText2 by remember { mutableStateOf(options2[0]) }
            var mTextFieldSize2 by remember { mutableStateOf(Size.Zero) }

            Column {
                OutlinedTextField(
                    value = selectedOptionText2,
                    onValueChange = { selectedOptionText2 },
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize2 = coordinates.size.toSize()
                        },
                    label = { Text("Status") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "contentDescription",
                            modifier = Modifier.clickable {
                                expanded2 = !expanded2
                            }
                        )
                    },
                    readOnly = true,
                )
                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current)
                        { mTextFieldSize2.width.toDp() })
                ) {
                    options2.forEach { selectionOption2 ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText2 = selectionOption2
                                expanded2 = false
                            },
                            text =  { Text(selectionOption2) }
                        )
                    }
                }
            }

            OutlinedTextField(value = mDate,
                onValueChange = { mDate = it }, label = { Text("Birth") }, trailingIcon = {
                    Icon(Icons.Default.DateRange,"date picker",
                        Modifier.clickable { mDatePickerDialog.show() })
                })
            ElevatedButton(modifier = Modifier
                .width(280.dp)
                .padding(PaddingValues(top = 30.dp)), colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue), onClick = {
                val baseUrl = "http://10.0.2.2:1337/api/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RegisterService::class.java)
                val statuswork: String = if(selectedOptionText == "Editor") {
                    "prepare"
                } else {
                    "null"
                }
                val call = retrofit.saveData(RegisterData(email.text, username.text, password.text, alamat.text, selectedOptionText, selectedOptionText2, mDate, statuswork))
                call.enqueue(object : Callback<LoginRespon> {
                    override fun onResponse(
                        call: Call<LoginRespon>,
                        response: Response<LoginRespon>
                    ) {
                        if (response.isSuccessful) {
                            navController.navigate("greeting")
                        } else {
                            print("error register")
                            Toast.makeText(
                                context,
                                "Kolom Harus Terisi Semua",
                                Toast.LENGTH_SHORT
                            ).show()
//                            try{
//                                val JObjError = JSONObject(response.errorBody()!!.string())
//                                Toast.makeText(
//                                    context,
//                                    JObjError.getJSONObject("error").get("message").toString(),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }catch (e: Exception){
//                                Toast.makeText(
//                                    context,
//                                    e.message.toString(),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                        print(t.message)
                    }

                })
            }) {
                Text("Buat Akun")
            }
        }
    }
}

