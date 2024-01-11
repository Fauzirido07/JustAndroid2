package com.example.justandroid2.frontend

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.justandroid2.PreferencesManager
import com.example.justandroid2.R
import com.example.justandroid2.data.jadwalData
import com.example.justandroid2.data.jadwalDataWrapper
import com.example.justandroid2.data.updateStatus
import com.example.justandroid2.respon.Jadwal
import com.example.justandroid2.respon.JadwalResponse
import com.example.justandroid2.respon.LoginRespon
import com.example.justandroid2.respon.UserRespon
import com.example.justandroid2.service.CVService
import com.example.justandroid2.service.JadwalService
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
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageEditor(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }

    val primaryColor = Color(0xFF596FB7)

    val listJadwal = remember { mutableStateListOf<JadwalResponse>() }
    val baseUrl = "http://10.0.2.2:1337/api/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JadwalService::class.java)
    val iduser = preferencesManager.getData("iduser")
    val call = retrofit.getAllTawaran(iduser, "*")
    call.enqueue(object : Callback<Jadwal<List<JadwalResponse>>> {
        override fun onResponse(
            call: Call<Jadwal<List<JadwalResponse>>>,
            response: Response<Jadwal<List<JadwalResponse>>>
        ) {
            if (response.code() == 200) {
                val resp = response.body()?.data!!
                listJadwal.clear()
                response.body()?.data!!.forEach { jadwalResp ->
                    listJadwal.add(jadwalResp)
                }
            } else if (response.code() == 400) {
                Toast.makeText(
                    context,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<Jadwal<List<JadwalResponse>>>, t: Throwable) {
            print(t.message)
        }

    })

    val urlProfile = remember { mutableStateOf("") }
    val sw = remember { mutableStateOf("") }
    val retrofit12 = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)
    val call12 = retrofit12.getDataEditor(iduser.toInt(), "*")
    call12.enqueue(object : Callback<List<UserRespon>> {
        override fun onResponse(
            call12: Call<List<UserRespon>>,
            response12: Response<List<UserRespon>>
        ) {
            if (response12.code() == 200) {
                val resp = response12.body()
                resp?.let { dataList ->
                    sw.value = dataList.get(0).statuswork

                    if(dataList.get(0).profile != null) {
                    urlProfile.value = dataList.get(0).profile?.url!!
                    println(urlProfile.value)
                    } else {
                        urlProfile.value = "/uploads/camera_690aedcd4f.png"
                    }

                }
            } else if (response12.code() == 400) {
                Toast.makeText(
                    context,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call12: Call<List<UserRespon>>, t: Throwable) {
            print(t.message)
        }

    })
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                preferencesManager.saveData("jwt", "")
                preferencesManager.saveData("job", "")
                navController.navigate("greeting")
            }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "logout")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Pick Me Up - Editor") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White,
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

                println("http://10.0.2.2:1337"+urlProfile.value)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    Column(modifier = Modifier
                        .size(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    )
                    {
                        Box(modifier = Modifier
                            .height(150.dp)
                            .width(150.dp)

                            , contentAlignment = Alignment.Center) {
                                Image(
                                    painter = rememberAsyncImagePainter("http://10.0.2.2:1337${urlProfile.value}"),
                                    contentDescription = "Selected Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(200.dp)
                                )
                        }

                    }


                }
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
                    text = preferencesManager.getData("status") + " - " + preferencesManager.getData(
                        "job"
                    ),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 17.64.sp,
                        color = Color(0x801E1E1E),
                    )
                )
                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))

                Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                ) {
                    val currentValue = urlProfile.value
                    val editUrl = currentValue.replace("/uploads/", "::uploads::")
                    println(editUrl)
                    Button(
                        onClick = {
                            navController.navigate(
                                "editProfile/" + preferencesManager.getData("iduser") + "/" + preferencesManager.getData(
                                    "username"
                                ) + "/" + preferencesManager.getData("job") + "/" + preferencesManager.getData(
                                    "email"
                                ) + "/" + preferencesManager.getData("alamat") + "/" + preferencesManager.getData(
                                    "birth"
                                ) + "/" + preferencesManager.getData("status")  + "/" + editUrl
                            )
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .padding(start = 90.dp)
                    ) {
                        Text(text = "Edit Profile")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { navController.navigate("uploadcv/"+iduser) },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .padding(end = 90.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                        ),
                    ) {
                        Text(text = "Upload CV")
                    }

                }
                if(sw.value == ("prepare")){
                    Button(onClick = {
                        val retrofit177 = Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(UserService::class.java)
                        val call177 = retrofit177.saveStatus(iduser, updateStatus("open to work"))
                        call177.enqueue(object : Callback<LoginRespon> {
                            override fun onResponse(
                                call177: Call<LoginRespon>,
                                response177: Response<LoginRespon>
                            ) {
                                if (response177.code() == 200) {
                                    val resp177 = response177.body()
                                    navController.navigate("homepageeditor")
                                } else if (response177.code() == 400) {
                                    Toast.makeText(
                                        context,
                                        "Error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call177: Call<LoginRespon>, t: Throwable) {
                                print(t.message)
                            }

                        })
                    }) {
                        Text(text = "Siap kerja")
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 36.dp),
                ) {
                    LazyColumn {
                        listJadwal.forEach { jadwalResp ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .background(Color.White),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .padding(horizontal = 20.dp),
                                        verticalArrangement = Arrangement.spacedBy(
                                            1.dp,
                                            Alignment.Top
                                        ),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = jadwalResp.attributes.id_user?.data?.attributes!!.username,
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                lineHeight = 17.64.sp,
                                                color = Color(0xFF1E1E1E),
                                            )
                                        )
                                        Text(
                                            text = "Link Meeting : "+ jadwalResp.attributes.link,
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                lineHeight = 17.64.sp,
                                                color = Color(0x801E1E1E),
                                            )
                                        )
                                        val rawDate = jadwalResp.attributes.date
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        val date = dateFormat.parse(rawDate)
                                        val indonesianLocale = Locale("id", "ID")
                                        val formattedDate = SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale).format(date)

                                        val rawTime = jadwalResp.attributes.time
                                        val timeParts = rawTime.split(":")
                                        val hours = timeParts[0].toInt()
                                        val minutes = timeParts[1].toInt()
                                        val formattedTime = String.format("%02d:%02d", hours, minutes)
                                        Text(
                                            text = "Date and Time : "+ formattedDate + " - " + formattedTime,
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                lineHeight = 17.64.sp,
                                                color = Color(0x801E1E1E),
                                            )
                                        )
                                    }
                                    val intent =
                                        remember { Intent(Intent.ACTION_VIEW, Uri.parse(jadwalResp.attributes.link)) }
                                    if(jadwalResp.attributes.tawaran == "terima"){
                                        Button(
                                            onClick = {
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(50.dp),
                                            modifier = Modifier
                                                .height(50.dp)
                                                .weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Green,
                                            ),
                                        ) {
                                            Text(text = "Mulai Meeting")
                                        }
                                    } else if(jadwalResp.attributes.tawaran == "tolak") {
                                        Button(
                                            onClick = {
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(50.dp),
                                            modifier = Modifier
                                                .height(50.dp)
                                                .weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Red,
                                            ),
                                        ) {
                                            Text(text = "Ditolak")
                                        }
                                    } else {
                                        IconButton(
                                            onClick = {
                                                val retrofit27 = Retrofit.Builder()
                                                    .baseUrl(baseUrl)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build()
                                                    .create(JadwalService::class.java)
                                                val jadwal27 = jadwalDataWrapper(
                                                    jadwalData(
                                                        id_user = jadwalResp.attributes.id_user?.data?.id!!.toInt(),
                                                        editor = jadwalResp.attributes.editor?.data?.id!!.toInt(),
                                                        date = jadwalResp.attributes.date,
                                                        time = jadwalResp.attributes.time,
                                                        link = jadwalResp.attributes.link,
                                                        tawaran = "terima"
                                                    )
                                                )
                                                println(jadwalResp.id.toString()+"HAHAHAHAHHHA22222")
                                                val call27 = retrofit27.updateTawaran(jadwalResp.id.toString(), jadwal27)
                                                call27.enqueue(object : Callback<JadwalResponse> {
                                                    override fun onResponse(
                                                        call27: Call<JadwalResponse>,
                                                        response27: Response<JadwalResponse>
                                                    ) {
                                                        if (response27.code() == 200) {
                                                            val retrofit17 = Retrofit.Builder()
                                                                .baseUrl(baseUrl)
                                                                .addConverterFactory(GsonConverterFactory.create())
                                                                .build()
                                                                .create(UserService::class.java)
                                                            println(jadwalResp.attributes.editor?.data?.id!!.toString()+"HAHAHAHAHHHA")
                                                            val call17 = retrofit17.saveStatus(jadwalResp.attributes.editor?.data?.id!!.toString(), updateStatus("closed"))
                                                            call17.enqueue(object : Callback<LoginRespon> {
                                                                override fun onResponse(
                                                                    call17: Call<LoginRespon>,
                                                                    response17: Response<LoginRespon>
                                                                ) {
                                                                    if (response17.code() == 200) {
                                                                        val resp17 = response17.body()
                                                                        navController.navigate("homepageeditor")
                                                                    } else if (response17.code() == 400) {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Error",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    }
                                                                }

                                                                override fun onFailure(call17: Call<LoginRespon>, t: Throwable) {
                                                                    print(t.message)
                                                                }

                                                            })
                                                        } else if (response27.code() == 400) {
                                                            Toast.makeText(
                                                                context,
                                                                "Error",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                    override fun onFailure(call27: Call<JadwalResponse>, t: Throwable) {
                                                        print(t.message)
                                                    }

                                                })
                                            },
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Detail Editor",
                                                tint = Color.Black
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                val retrofit24 = Retrofit.Builder()
                                                    .baseUrl(baseUrl)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build()
                                                    .create(JadwalService::class.java)
                                                val jadwal = jadwalDataWrapper(
                                                    jadwalData(
                                                        id_user = jadwalResp.attributes.id_user?.data?.id!!.toInt(),
                                                        editor = jadwalResp.attributes.editor?.data?.id!!.toInt(),
                                                        date = jadwalResp.attributes.date,
                                                        time = jadwalResp.attributes.time,
                                                        link = jadwalResp.attributes.link,
                                                        tawaran = "tolak"
                                                    )
                                                )
                                                val call24 = retrofit24.updateTawaran(jadwalResp.id.toString(), jadwal)
                                                call24.enqueue(object : Callback<JadwalResponse> {
                                                    override fun onResponse(
                                                        call24: Call<JadwalResponse>,
                                                        response24: Response<JadwalResponse>
                                                    ) {
                                                        if (response24.code() == 200) {
                                                            val resp = response24.body()
                                                        } else if (response24.code() == 400) {
                                                            Toast.makeText(
                                                                context,
                                                                "Error",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                    override fun onFailure(call24: Call<JadwalResponse>, t: Throwable) {
                                                        print(t.message)
                                                    }

                                                })
                                            },
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Detail Editor",
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadCV(navController: NavController, id:String?, context: Context = LocalContext.current)
{
    val idCV = remember {
        mutableStateOf( id ?: "")
    }
    val primaryColor = Color(0xFF596FB7)
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
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(text = "Upload CV") },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = primaryColor,
                titleContentColor = Color.White,),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = Color.Black
                    )
                }
            })}
) {innerPadding ->
    Column(modifier = Modifier.padding(innerPadding)) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier
                .size(900.dp)
                .background(Color(0xFFE0E0E0))
                .clip(RoundedCornerShape(8.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                Box(modifier = Modifier
                    .height(430.dp)
                    .width(430.dp)
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
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .size(48.dp))
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

                Button(
                    onClick = {
                        val file = selectedImageFile
                        val mimeType =
                            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                file!!.extension
                            )
                        val refRequestBody =
                            "plugin::users-permissions.user".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val refIdRequestBody = idCV.value
                            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val fieldRequestBody =
                            "cv".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val fileRequestBody = MultipartBody.Part.createFormData(
                            "files",
                            file.name,
                            file.asRequestBody(mimeType?.toMediaTypeOrNull())
                        )

                        val retrofit2 = Retrofit.Builder().baseUrl("http://10.0.2.2:1337/api/")
                            .addConverterFactory(GsonConverterFactory.create()).client(
                                OkHttpClient.Builder().addInterceptor(
                                    HttpLoggingInterceptor().setLevel(
                                        HttpLoggingInterceptor.Level.BODY
                                    )
                                ).build()
                            )
                            .build().create(CVService::class.java)
                        val call2 = retrofit2.uploadImage(
                            refRequestBody,
                            refIdRequestBody,
                            fieldRequestBody,
                            fileRequestBody
                        )
                        call2.enqueue(object : Callback<UploadResponseList> {
                            override fun onResponse(
                                call12: Call<UploadResponseList>,
                                response12: Response<UploadResponseList>
                            ) {
                                if (response12.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Berhasil menambahkan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("homepageeditor")
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error: ${response12.code()} - ${response12.message()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(
                                call12: Call<UploadResponseList>, t: Throwable
                            ) {
                                Toast.makeText(
                                    context,
                                    "Error: " + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)) {
                    Text(text = "Upload CV disini")
                }

            }



        }
    }
}
}