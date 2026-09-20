plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:group:group-domain"))
    implementation(project(":core:domain"))

    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}
