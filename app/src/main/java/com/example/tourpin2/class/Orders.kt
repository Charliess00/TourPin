package com.example.tourpin2.`class`

data class Orders(
    val country: String,
    val city: String,
    val data: String,
    val nightFirst: Int,
    val nightSecond: Int,
    var tourists_count: Int,
    var uid: String
) {
    constructor() : this("", "", "", 0, 0, 0, "")
}
