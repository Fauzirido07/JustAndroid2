package com.example.justandroid2.respon

import com.example.justandroid2.service.UploadResponse
import com.google.gson.annotations.SerializedName

enum class UserRole {
    Perekrut, Editor
}

class UserRespon {
    var id: Int = 0
    var username : String = ""
    var email : String = ""
    var provider : String = ""
    var confirmed: String = ""
    var blocked: Boolean = false
    var createdAt : String = ""
    var updatedAt: String = ""
    var alamat: String = ""
    var job: String = ""
    var status: String = ""
    var birth: String = ""
    @SerializedName("cv")
    var cv: UploadResponse? = null
    @SerializedName("profile")
    var profile: UploadResponse? = null
//    var status: UserRole = UserRole.Perekrut
}

class User{
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("attributes")
    var attributes:UserAttributes = UserAttributes()
}

class UserAttributes {
    @SerializedName("username")
    var username : String = ""
    @SerializedName("email")
    var email : String = ""
    @SerializedName("provider")
    var provider : String = ""
    @SerializedName("confirmed")
    var confirmed: String = ""
    @SerializedName("blocked")
    var blocked: Boolean = false
    @SerializedName("createdAt")
    var createdAt : String = ""
    @SerializedName("updatedAt")
    var updatedAt: String = ""
    @SerializedName("alamat")
    var alamat: String = ""
    @SerializedName("job")
    var job: String = ""
    @SerializedName("status")
    var status: String = ""
    @SerializedName("birth")
    var birth: String = ""
}