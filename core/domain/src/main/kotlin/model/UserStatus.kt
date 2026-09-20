package model

enum class UserStatus {
    ACTIVE, INACTIVE, AWAITING_CONFIRMATION;

    val dbValue: String get() = name.uppercase()

    companion object {
        fun fromDbValue(value: String): UserStatus = UserStatus.valueOf(value.uppercase())
    }
}