plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:trip:trip-domain"))
    implementation(project(":features:group:group-domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:email"))

    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}
