package com.example.tourpin2.`class`

data class Proposals(
    val hotel_name: String,
    val hotel_desc: String,
    val hotel_img: String,
    val country: String,
    val city_tour: String,
    val data_start: String,
    val data_end: String,
    val night: Int,
    val person: Int,
    val city: String,
    val price: Long,
    val booking: Boolean,
    val order_ID: String
){
    constructor() : this("","","","","","","",0,0,"",0,false,"")
}
