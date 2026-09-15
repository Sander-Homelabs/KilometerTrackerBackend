plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":features:group:group-domain"))
    implementation(project(":features:user:user-domain"))
    implementation(project(":features:trip:trip-domain"))

    implementation(libs.bundles.exposed)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)
    implementation(libs.postgresql)
    implementation(libs.hikari)
}
