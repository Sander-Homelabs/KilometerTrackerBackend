package database.user

import UserPasswordRepository
import database.dbQueryAs
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class UserPasswordRepositoryImpl(private val db: Database): UserPasswordRepository {
    override suspend fun findByEmail(email: String): String? = dbQueryAs(email, db) {
        UserPasswordTable.selectAll()
            .where { UserPasswordTable.email eq email }
            .map { it[UserPasswordTable.email] }
            .singleOrNull()
    }

    override suspend fun save(password: String, email: String) {
        dbQueryAs(email, db) {
            UserPasswordTable.insert {
                it[UserPasswordTable.password] = password
                it[UserPasswordTable.email] = email
            }
        }
    }

    override suspend fun disable(email: String) {
        dbQueryAs(email, db) {
            UserPasswordTable.update(where = { UserPasswordTable.email eq email }) {
                it[UserPasswordTable.active] = false
            }
        }
    }
}