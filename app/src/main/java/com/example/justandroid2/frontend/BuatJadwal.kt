package com.example.justandroid2.frontend

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.justandroid2.PreferencesManager
import com.example.justandroid2.data.jadwalData
import com.example.justandroid2.data.jadwalDataWrapper
import com.example.justandroid2.respon.JadwalResponse
import com.example.justandroid2.service.JadwalService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatJadwal(navController: NavController, id: String?, context: Context = LocalContext.current){
    val preferencesManager = remember { PreferencesManager(context = context) }

    var linkMeet by remember { mutableStateOf("") }
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
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val formattedMonth = String.format("%02d", mMonth + 1)
            val formattedDay = String.format("%02d", mDayOfMonth)
            mDate = "$mYear-$formattedMonth-$formattedDay"
        }, mYear, mMonth, mDay
    )

    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    var mTime by remember { mutableStateOf("Select Time") }

    val mTimePickerDialog = TimePickerDialog(
        context,
        {_, mHour : Int, mMinute: Int ->
            mTime = String.format("%02d:%02d:00.000", mHour, mMinute)
        }, mHour, mMinute, true
    )

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                    Text(text = "Buat Jadwal") }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },) {
            innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues(top = 100.dp))
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(value = mDate,
                onValueChange = { mDate = it }, label = { Text("Select Date") }, trailingIcon = {
                    Icon(Icons.Default.DateRange,"date picker",
                        Modifier.clickable { mDatePickerDialog.show() })
                })
            OutlinedTextField(value = mTime,
                onValueChange = { mTime = it }, label = { Text("Select Time") }, trailingIcon = {
                    Icon(Icons.Default.Add,"time picker",
                        Modifier.clickable { mTimePickerDialog.show() })
                })
            OutlinedTextField(value = linkMeet, onValueChange = { newText ->
                linkMeet = newText
            }, label = { Text("Masukkan Link Meet/Zoom") })
            Button(onClick = {
                var baseUrl = "http://10.0.2.2:1337/api/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(JadwalService::class.java)
                val call = retrofit.buatJadwal(
                    jadwalDataWrapper(
                        jadwalData(
                            id_user = preferencesManager.getData("iduser").toInt(),
                            editor = id!!.toInt(),
                            date = mDate,
                            time = mTime,
                            link = linkMeet
                        )
                    )
                )
                call.enqueue(object : Callback<JadwalResponse> {
                    override fun onResponse(
                        call: Call<JadwalResponse>,
                        response: Response<JadwalResponse>
                    ) {
                        if (response.code() == 200) {
                            navController.navigate("Homepage")
                        } else if (response.code() == 400) {
                            print("error register")
                            var toast = Toast.makeText(
                                context,
                                "Kolom Harus Terisi Semua",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<JadwalResponse>, t: Throwable) {
                        print(t.message)
                    }

                })
            }, modifier = Modifier.padding(top = 20.dp)) {
                Text(text = "Buat Jadwal")
            }
        }
    }

}

