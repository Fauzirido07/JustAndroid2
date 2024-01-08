package com.example.justandroid2.frontend

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.justandroid2.respon.LoginRespon
import com.example.justandroid2.PreferencesManager
import com.example.justandroid2.R
import com.example.justandroid2.data.UpdateData
import com.example.justandroid2.service.CVService
import com.example.justandroid2.service.UploadResponseList
import com.example.justandroid2.service.UserService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserPage(navController: NavController, userid : String?, username : String?, job : String?, email : String?, alamat : String?, birth : String?, status : String?, editUrl : String?, context: Context = LocalContext.current){
    val preferencesManager = remember { PreferencesManager(context = context) }
    var username by remember { mutableStateOf(TextFieldValue(username ?: "")) }
    var email by remember { mutableStateOf(TextFieldValue(email ?: "")) }
    var alamat by remember { mutableStateOf(TextFieldValue(alamat ?: "")) }
    var iduser = userid ?: ""
    val currentValue = editUrl ?: ""
    val newUrl = currentValue.replace("::uploads::", "/uploads/")
    println(newUrl)
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
//    val mDate = remember { mutableStateOf("") }
    var mDate by remember { mutableStateOf(birth ?: "Select Date") }
    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate = "$mYear-${mMonth+1}-$mDayOfMonth"
        }, mYear, mMonth, mDay
    )

    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val resolver = context.contentResolver
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                resolver.openInputStream(selectedImageUri!!)?.let { inputStream ->
                    val originalFileName = context.contentResolver.query(
                        selectedImageUri!!, null, null, null, null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val displayNameIndex =
                                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (displayNameIndex != -1) {
                                cursor.getString(displayNameIndex)
                            } else {
                                null
                            }
                        } else {
                            null
                        }
                    }
                    val file = File(context.cacheDir, originalFileName ?: "temp_img.jpg")
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    selectedImageFile = file
                }
            }
        })

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    Text(text = "Edit Profile") }
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
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {


                Column(modifier = Modifier
                    .size(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {
                    Box(modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .clickable { pickImageLauncher.launch("image/*") }
                        , contentAlignment = Alignment.Center) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberImagePainter(data = selectedImageUri, builder = {
                                    transformations(CircleCropTransformation())
                                }),
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter("http://10.0.2.2:1337$newUrl"),
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .size(200.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (selectedImageUri != null) {
                        IconButton(
                            onClick = { selectedImageUri = null }, modifier = Modifier.size(48.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Image")
                        }
                    }
                }


            }
            OutlinedTextField(modifier = Modifier.padding(PaddingValues(top = 8.dp)), value = username, onValueChange = { newText ->
                username = newText
            }, label = { Text("Username") })
            OutlinedTextField(value = email, onValueChange = { newText ->
                email = newText
            }, label = { Text("Email") })
            OutlinedTextField(value = alamat, onValueChange = { newText ->
                alamat = newText
            }, label = { Text("Address") }, singleLine = false)

            val options = listOf("Editor", "Perekrut")
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf( job ?: options[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = {},
                    label = { Text("Job") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            },
                        )
                    }
                }
            }

            val options2 = listOf("Tetap", "Freelance")
            var expanded2 by remember { mutableStateOf(false) }
            var selectedOptionText2 by remember { mutableStateOf( status ?: options2[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded2,
                onExpandedChange = { expanded2 = !expanded2 },
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText2,
                    onValueChange = {},
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                )
                ExposedDropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                ) {
                    options2.forEach { selectionOption2 ->
                        DropdownMenuItem(
                            text = { Text(selectionOption2) },
                            onClick = {
                                selectedOptionText2 = selectionOption2
                                expanded2 = false
                            },
                        )
                    }
                }
            }
                OutlinedTextField(value = mDate,
                onValueChange = { mDate = it }, label = { Text("Selected Date") }, trailingIcon = {
                        Icon(Icons.Default.DateRange,"date picker",
                            Modifier.clickable { mDatePickerDialog.show() })
                    })

            ElevatedButton(modifier = Modifier.width(280.dp),colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue), onClick = {
                val baseUrl = "http://10.0.2.2:1337/api/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UserService::class.java)
                val call = retrofit.save(iduser, UpdateData(email.text, username.text, alamat.text, selectedOptionText, selectedOptionText2, mDate))
                call.enqueue(object : Callback<LoginRespon> {
                    override fun onResponse(
                        call: Call<LoginRespon>,
                        response: Response<LoginRespon>
                    ) {
                        if (response.code() == 200) {
                            preferencesManager.saveData("username", username.text)
                            preferencesManager.saveData("job", selectedOptionText)
                            preferencesManager.saveData("email", email.text)
                            preferencesManager.saveData("alamat", alamat.text)
                            preferencesManager.saveData("status", selectedOptionText2)
                            preferencesManager.saveData("birth", mDate)
                            val file = selectedImageFile
                            val mimeType =
                                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                    file!!.extension
                                )
                            val refRequestBody =
                                "plugin::users-permissions.user".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val refIdRequestBody = iduser
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val fieldRequestBody =
                                "profile".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val fileRequestBody = MultipartBody.Part.createFormData(
                                "files",
                                file.name,
                                file.asRequestBody(mimeType?.toMediaTypeOrNull())
                            )

                            val retrofit23 = Retrofit.Builder().baseUrl("http://10.0.2.2:1337/api/")
                                .addConverterFactory(GsonConverterFactory.create()).client(
                                    OkHttpClient.Builder().addInterceptor(
                                        HttpLoggingInterceptor().setLevel(
                                            HttpLoggingInterceptor.Level.BODY
                                        )
                                    ).build()
                                )
                                .build().create(CVService::class.java)
                            val call23 = retrofit23.uploadImage(
                                refRequestBody,
                                refIdRequestBody,
                                fieldRequestBody,
                                fileRequestBody
                            )
                            call23.enqueue(object : Callback<UploadResponseList> {
                                override fun onResponse(
                                    call23: Call<UploadResponseList>,
                                    response23: Response<UploadResponseList>
                                ) {
                                    if (response23.isSuccessful) {
                                        navController.navigate("homepageeditor")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error: ${response23.code()} - ${response23.message()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                override fun onFailure(
                                    call23: Call<UploadResponseList>, t: Throwable
                                ) {
                                    Toast.makeText(
                                        context,
                                        "Error: " + t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                        else if (response.code() == 400) {
                            var toast = Toast.makeText(
                                context,
                                "Kolom Harus Terisi Semua",
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
        }

    }
}
