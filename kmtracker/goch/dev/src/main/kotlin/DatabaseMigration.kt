import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource
import java.net.URI

fun main() {
    val databaseUrl = System.getenv("DATABASE_URL_ADMIN")
        ?: error("DATABASE_URL_ADMIN is not set")

    val uri = URI(databaseUrl)

    val dataSource = PGSimpleDataSource().apply {
        serverNames = arrayOf(uri.host)
        portNumbers = intArrayOf(uri.port)
        databaseName = uri.path.removePrefix("/")

        val userInfo = uri.userInfo
            ?: error("DATABASE_URL_ADMIN must contain username and password")

        val credentials = userInfo.split(":", limit = 2)

        if (credentials.size != 2) {
            error("DATABASE_URL_ADMIN must contain username and password")
        }

        user = credentials[0]
        password = credentials[1]
    }

    println("Running database migrations...")

    Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .schemas("kilometer_tracker")
        .defaultSchema("kilometer_tracker")
        .load()
        .migrate()

    println("Database migrations completed.")
}
