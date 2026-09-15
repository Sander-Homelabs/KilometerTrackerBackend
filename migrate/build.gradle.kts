plugins {
    id("kotlin-common")
    application
}

application {
    mainClass.set("MigrateKt")
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:common"))
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)
    implementation(libs.postgresql)
    implementation(libs.hikari)
    implementation(libs.logback.classic)
    implementation(libs.dotenv.kotlin)
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir

    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.readLines()
            .filter { it.isNotBlank() && !it.trimStart().startsWith("#") && it.contains("=") }
            .forEach { line ->
                val idx = line.indexOf("=")
                val key = line.substring(0, idx).trim()
                val value = line.substring(idx + 1).trim()
                environment(key, value)
            }
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "MigrateKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}