package com.example.tourpin2.`class`

data class Tours(
    val hotel_name: String,
    val hotel_desc: String,
    val country: String,
    val city_tour: String,
    val data_start: String,
    val night: Int,
    val person: Int,
    val hotel_img: String,
    val price: Int,
    val uid: String,
    val additions: Additions
) {
    constructor() : this("", "", "", "", "", 0, 0, "", 0, "", Additions())
}

data class Additions(
    val flight: Boolean,
    val food: String,
    val habitation: String,
    val insurance: Boolean,
    val transfer: Boolean
) {
    constructor() : this(false, "", "", false, false)
}

