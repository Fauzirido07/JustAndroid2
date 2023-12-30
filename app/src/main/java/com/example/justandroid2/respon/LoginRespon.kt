package com.example.justandroid2.respon

import com.google.gson.annotations.SerializedName

class LoginRespon {
    @SerializedName("jwt")
    var jwt : String = ""
    var user: UserRespon? = null
}