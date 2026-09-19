plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":features:user:user-domain"))
    implementation(project(":features:group:group-domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:email"))
    implementation(project(":core:security"))
}
