package model

enum class GroupRole {
    MEMBER, ADMIN;

    val dbValue: String get() = name.uppercase()

    companion object {
        fun fromDbValue(value: String): GroupRole = valueOf(value.uppercase())
    }
}