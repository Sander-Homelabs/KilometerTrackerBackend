
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "dev.goch"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.netty)

    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)

    implementation(libs.r2dbc.postgresql)

    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    implementation(libs.h2database.h2)
    implementation(libs.h2database.r2dbc)
    implementation(libs.logback.classic)
    implementation(libs.postgresql)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}

fun loadDotEnv(): Map<String, String> {
    val envFile = rootProject.file("../../..").resolve(".env")

    if (!envFile.exists()) {
        return emptyMap()
    }

    return envFile.readLines()
        .filter { it.isNotBlank() && !it.trimStart().startsWith("#") }
        .mapNotNull { line ->
            val index = line.indexOf('=')

            if (index <= 0) {
                null
            } else {
                val key = line.substring(0, index).trim()
                val value = line.substring(index + 1).trim()
                    .removeSurrounding("\"")
                    .removeSurrounding("'")

                key to value
            }
        }
        .toMap()
}

tasks.register<JavaExec>("migrate") {
    group = "database"
    description = "Run database migrations"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("DatabaseMigrationKt")

    val dotEnv = loadDotEnv()

    environment(
        dotEnv.filterKeys { key ->
            System.getenv(key) == null
        }
    )
}

tasks.named<JavaExec>("run") {
    val dotEnv = loadDotEnv()

    environment(
        dotEnv.filterKeys { key ->
            System.getenv(key) == null
        }
    )
}
