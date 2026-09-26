plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.jwt)
    implementation(libs.bcrypt)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}
