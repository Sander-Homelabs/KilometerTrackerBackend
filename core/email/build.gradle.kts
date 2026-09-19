plugins {
    id("kotlin-common")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.knock)
    implementation(libs.dotenv.kotlin)
}
