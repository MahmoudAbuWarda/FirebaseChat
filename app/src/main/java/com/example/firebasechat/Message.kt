package com.example.firebasechat

import com.google.firebase.firestore.Exclude

data class Message(var username:String?=null,var nickname:String?=null, var msg: String?=null, var time: Long?=null, var status:Int=1,var messageType:Int=1,var imageUrl:String?=null)
