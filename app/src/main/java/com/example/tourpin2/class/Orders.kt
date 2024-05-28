data class Orders(
    val country: String,
    val city: String,
    val data: String,
    val nightFirst: String, // Заменено на String
    val nightSecond: String, // Заменено на String
    var tourists_count: String,
    var uid: String
) {
    constructor() : this("", "", "", "", "", "", "")
}
