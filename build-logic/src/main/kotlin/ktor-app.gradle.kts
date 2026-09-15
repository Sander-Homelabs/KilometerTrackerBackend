import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("io.ktor.plugin")
}

configure<KotlinJvmProjectExtension> {
    jvmToolchain(21)
}