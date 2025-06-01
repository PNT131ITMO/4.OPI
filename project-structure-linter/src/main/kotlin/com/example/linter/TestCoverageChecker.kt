package com.example.linter

import org.gradle.api.Project
import java.io.File

class TestCoverageChecker(private val config: LinterExtension) {
    fun run(project: Project): List<String> {
        val results = mutableListOf<String>()
        if (!config.requireTestCoverage) return results

        project.subprojects.forEach { sub ->
            val testDir = File(sub.projectDir, "src/test/java")
            if (!testDir.exists()) {
                results += "${sub.name}: Missing src/test/java"
            }
        }
        return results
    }
}