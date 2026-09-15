plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
}
