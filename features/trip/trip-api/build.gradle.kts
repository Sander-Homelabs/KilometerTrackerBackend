plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:trip:trip-service"))
    implementation(project(":features:trip:trip-domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:security"))
    implementation(libs.bundles.ktor.server)
}
