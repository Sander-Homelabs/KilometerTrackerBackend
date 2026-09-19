import exception.InvalidPassword

fun String.validatePassword() {
    val reasons: MutableList<String> = mutableListOf()
    if (this.length < 10) reasons.add("Be at least 10 characters long")
    if (this.length > 50) reasons.add("Be maximum of 50 characters long")
    if (!this.contains(Regex("[@#$%^&+=]"))) reasons.add("Contain at least 1 special character")
    if (!this.contains(Regex("[A-Z]"))) reasons.add("Contain at least 1 uppercase letter")
    if (!this.contains(Regex("[a-z]"))) reasons.add("Contain at least 1 lowercase letter")
    if (reasons.isNotEmpty()) throw InvalidPassword(reasons)
}
