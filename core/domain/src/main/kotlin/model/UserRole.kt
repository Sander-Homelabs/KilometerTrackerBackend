package model

enum class UserRole {
    USER, ADMIN;

    val dbValue: String get() = name.lowercase()

    companion object {
        fun fromDbValue(value: String): UserRole = valueOf(value.uppercase())
    }
}