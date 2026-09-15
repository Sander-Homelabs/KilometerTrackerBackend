plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:trip:trip-domain"))
    implementation(project(":features:group:group-domain"))
}
