package com.example.linter

open class LinterExtension {
    var allowedDependencies: Map<String, List<String>> = emptyMap()
    var namingRules: Map<String, String> = emptyMap()
    var enforceDirectoryStructure: Boolean = true
    var checkForSecrets: Boolean = true
    var requireTestCoverage: Boolean = true
    var failOnViolation: Boolean = true
    var exportReport: String = "build/linter-report.json"
}