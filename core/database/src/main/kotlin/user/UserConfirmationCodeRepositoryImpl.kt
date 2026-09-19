package database.user

import UserConfirmationCodeRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class UserConfirmationCodeRepositoryImpl(private val db: Database): UserConfirmationCodeRepository {
    override suspend fun findByEmail(email: String): UUID? =
        newSuspendedTransaction(db = db) {
            UserConfirmationCodeTable.selectAll()
                .where { UserConfirmationCodeTable.email eq email }
                .map { it[UserConfirmationCodeTable.code] }
                .singleOrNull()
        }

    override suspend fun save(email: String, code: UUID) {
        newSuspendedTransaction(db = db) {
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
}