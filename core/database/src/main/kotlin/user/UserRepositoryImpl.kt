package database.user

import UserRepository
import database.dbQueryAs
import model.PendingUser
import model.User
import model.UserAccount
import model.UserRole
import model.UserStatus
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl(private val db: Database): UserRepository {
    override suspend fun findByEmail(email: String): UserAccount? = dbQueryAs(email, db) {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .map { it.toDomain()}
            .singleOrNull()
    }

    override suspend fun findActiveByEmail(email: String): User? =
        findByEmail(email) as? User

    override suspend fun save(user: PendingUser) {
        newSuspendedTransaction(db = db) {
            UsersTable.insert {
                it[UsersTable.email] = user.email
                it[UsersTable.status] = UserStatus.AWAITING_CONFIRMATION.dbValue
                it[UsersTable.role] = UserRole.USER.dbValue
            }
        }
    }

    override suspend fun update(email: String, user: User) {
        dbQueryAs(email, db) {
            UsersTable.update({ UsersTable.email eq email }) {
                it[UsersTable.firstName] = user.firstName
                it[UsersTable.lastName] = user.lastName
            }
        }
    }

    override suspend fun disable(email: String) {
        dbQueryAs(email, db) {
            UsersTable.update({ UsersTable.email eq email }) {
                it[UsersTable.status] = UserStatus.INACTIVE.dbValue
            }
        }
    }

    override suspend fun activate(email: String, user: User) {
        dbQueryAs(email, db) {
            UsersTable.update({ UsersTable.email eq email }) {
                it[UsersTable.status] = UserStatus.ACTIVE.dbValue
                it[UsersTable.firstName] = user.firstName
                it[UsersTable.lastName] = user.lastName
            }
        }
    }
}

private fun ResultRow.toDomain(): UserAccount {
    val firstName = this[UsersTable.firstName]
    val lastName = this[UsersTable.lastName]

    return if (firstName != null && lastName != null) {
        User(
            email = this[UsersTable.email],
            firstName = firstName,
            lastName = lastName,
            status = UserStatus.fromDbValue(this[UsersTable.status]),
            role = UserRole.fromDbValue(this[UsersTable.role])
        )
    } else {
        PendingUser(
            email = this[UsersTable.email],
            status = UserStatus.fromDbValue(this[UsersTable.status]),
            role = UserRole.fromDbValue(this[UsersTable.role])
        )
    }
}