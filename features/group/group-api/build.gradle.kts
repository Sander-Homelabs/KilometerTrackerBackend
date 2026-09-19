plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:group:group-service"))
    implementation(project(":features:group:group-domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:security"))
    implementation(libs.bundles.ktor.server)
}
