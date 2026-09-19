package database.user

import UserConfirmationCodeRepository
import model.UserConfirmationCode
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class UserConfirmationCodeRepositoryImpl(private val db: Database): UserConfirmationCodeRepository {
    override suspend fun findByEmail(email: String): UserConfirmationCode? =
        newSuspendedTransaction(db = db) {
            UserConfirmationCodeTable.selectAll()
                .where { UserConfirmationCodeTable.email eq email }
                .map { it.toDomain() }
                .singleOrNull()
        }

    override suspend fun upsert(email: String, code: UUID) {
        newSuspendedTransaction(db = db) {
            UserConfirmationCodeTable.deleteWhere {
                UserConfirmationCodeTable.email eq email
            }
            UserConfirmationCodeTable.insert {
                it[UserConfirmationCodeTable.email] = email
                it[UserConfirmationCodeTable.code] = code
            }
        }
    }

    override suspend fun delete(email: String, code: UUID) {
        newSuspendedTransaction(db = db) {
            UserConfirmationCodeTable.deleteWhere {
                UserConfirmationCodeTable.email eq email
            }
        }
    }

    private fun ResultRow.toDomain(): UserConfirmationCode = UserConfirmationCode(
        email = this[UserConfirmationCodeTable.email],
        code = this[UserConfirmationCodeTable.code],
        createdAt = this[UserConfirmationCodeTable.createdAt]
    )
}