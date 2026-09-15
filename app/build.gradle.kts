plugins {
    id("ktor-app")
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:security"))
    implementation(project(":core:email"))
    implementation(project(":plugins"))

    implementation(project(":features:group:group-api"))
    implementation(project(":features:group:group-service"))
    implementation(project(":features:group:group-domain"))
    implementation(project(":features:user:user-api"))
    implementation(project(":features:user:user-service"))
    implementation(project(":features:user:user-domain"))
    implementation(project(":features:trip:trip-api"))
    implementation(project(":features:trip:trip-service"))
    implementation(project(":features:trip:trip-domain"))

    implementation(libs.bundles.ktor.server)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.logback.classic)
    implementation(libs.hikari)
}
