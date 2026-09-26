package database.user

import RefreshTokenRepository
import database.dbQueryAs
import model.RefreshToken
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class RefreshTokenRepositoryImpl(private val db: Database): RefreshTokenRepository {
    override suspend fun findByEmail(email: String): List<RefreshToken> =
        dbQueryAs(email, db) {
            RefreshTokenTable.selectAll()
                .where { RefreshTokenTable.email eq email }
                .map { it.toDomain() }
        }


    override suspend fun findActiveByEmail(email: String): List<RefreshToken.Active> =
        findByEmail(email).filterIsInstance<RefreshToken.Active>()

    override suspend fun save(email: String, token: String) {
        dbQueryAs(email, db) {
            RefreshTokenTable.insert {
                it[this.email] = email
                it[this.token] = token
            }
        }
    }

    override suspend fun disable(email: String, token: String) {
        dbQueryAs(email, db) {
            RefreshTokenTable.update(where = { RefreshTokenTable.token eq token }) {
                it[this.active] = false
            }
        }
    }
}

private fun ResultRow.toDomain(): RefreshToken {
    return if (this[RefreshTokenTable.active]) {
        RefreshToken.Active(
            email = this[RefreshTokenTable.email],
            token = this[RefreshTokenTable.token]
        )
    } else {
        RefreshToken.Inactive(
            email = this[RefreshTokenTable.email],
            token = this[RefreshTokenTable.token]
        )
    }
}