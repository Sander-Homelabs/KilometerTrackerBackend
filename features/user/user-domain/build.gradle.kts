plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":features:group:group-domain"))
}
