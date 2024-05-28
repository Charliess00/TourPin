data class User(
    val name: String,
    val surname: String,
    val phone: String,
    val email: String,
) {
    constructor() : this("", "", "", "")
}
