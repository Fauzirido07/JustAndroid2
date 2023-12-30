package com.example.justandroid2.respon

enum class UserRole {
    Admin, Perekrut, Editor
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
//    var status: UserRole = UserRole.Perekrut
}