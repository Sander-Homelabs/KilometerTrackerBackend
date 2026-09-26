plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:user:user-domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:security"))

    implementation(libs.bundles.ktor.server)
}
