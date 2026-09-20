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
    loadEnvFile(project)
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "MigrateKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}