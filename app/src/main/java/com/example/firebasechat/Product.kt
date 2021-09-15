package com.example.firebasechat

import com.google.firebase.firestore.Exclude

class Product (@get:Exclude var id:String?=null, var name:String?=null, var price:Float?=null, var category:String?=null) {
}