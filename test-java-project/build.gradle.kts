plugins {
    id("java")                     
    id("com.example.linter.plugin") version "1.0.0" 
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

projectLinter {
    namingRules = mapOf(
        "class" to "^[A-Z][a-zA-Z0-9]*$",
        "method" to "^[a-z][a-zA-Z0-9]*$",
        "variable" to "^[a-z][a-zA-Z0-9]*$"
    )
    allowedDependencies = mapOf(
        "app" to listOf("core", "utils")
    )
    enforceDirectoryStructure = true
    checkForSecrets = true
    requireTestCoverage = true
    failOnViolation = true
    exportReport = "build/linter-report.txt"
}
