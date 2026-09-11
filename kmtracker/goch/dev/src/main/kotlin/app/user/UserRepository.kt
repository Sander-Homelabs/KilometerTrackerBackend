package app.user

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

class UserRepository(
    private val database: R2dbcDatabase
) {
    fun getUser(email: String): String? {
        return null
    }

    fun addUser(email: String, password: String, firstName: String, lastName: String): Boolean {
        return true
    }
}