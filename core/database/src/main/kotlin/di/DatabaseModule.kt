package database.di

import org.koin.dsl.module
import org.jetbrains.exposed.sql.Database
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import database.user.UserRepositoryImpl
import database.user.UserConfirmationCodeRepositoryImpl
import UserRepository
import UserConfirmationCodeRepository
import UserPasswordRepository
import database.user.UserPasswordRepositoryImpl
import java.net.URI

val databaseModule = module {
    single<Database> {
        val databaseUrl = System.getenv("DATABASE_URL_API")
            ?: error("DATABASE_URL_API is not set")

        val uri = URI(databaseUrl)
        val userInfo = uri.userInfo
            ?: error("DATABASE_URL_API must contain username and password")

        val credentials = userInfo.split(":", limit = 2)
        if (credentials.size != 2) {
            error("DATABASE_URL_API must contain username and password")
        }

        val dataSource = HikariDataSource(HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
            username = credentials[0]
            password = credentials[1]
            schema = "kilometer_tracker"
        })

        Database.connect(dataSource)
    }

    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserPasswordRepository> { UserPasswordRepositoryImpl(get()) }
    single<UserConfirmationCodeRepository> { UserConfirmationCodeRepositoryImpl(get()) }
}