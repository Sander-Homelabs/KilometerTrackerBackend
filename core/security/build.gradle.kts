plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.bcrypt)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}
