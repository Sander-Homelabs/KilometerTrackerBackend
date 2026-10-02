plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:user:user-domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:email"))
    implementation(project(":core:security"))

    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.kotlinx.datetime)
}
