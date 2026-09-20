import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec

fun JavaExec.loadEnvFile(project: Project) {
    workingDir = project.rootProject.projectDir

    val envFile = project.rootProject.file(".env")
    if (envFile.exists()) {
        envFile.readLines()
            .filter { it.isNotBlank() && !it.trimStart().startsWith("#") && it.contains("=") }
            .forEach { line ->
                val idx = line.indexOf("=")
                val key = line.substring(0, idx).trim()
                val value = line.substring(idx + 1).trim()
                environment(key, value)
            }
    }
}