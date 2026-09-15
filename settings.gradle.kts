pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    ":app",
    ":migrate",
    ":core:domain",
    ":core:common",
    ":core:security",
    ":core:email",
    ":core:database",
    ":plugins",
    ":features:group:group-domain",
    ":features:group:group-service",
    ":features:group:group-api",
    ":features:user:user-domain",
    ":features:user:user-service",
    ":features:user:user-api",
    ":features:trip:trip-domain",
    ":features:trip:trip-service",
    ":features:trip:trip-api",
)

rootProject.name = "kmtracker"