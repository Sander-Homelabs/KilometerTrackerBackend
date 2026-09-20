plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:user:user-domain"))
    implementation(project(":core:domain"))

    implementation(libs.bundles.ktor.server)
}
