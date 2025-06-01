package com.example.linter

import org.gradle.api.Project
import java.io.File

class LintReport(private val config: LinterExtension) {
    fun export(project: Project, violations: List<String>) {
        val reportFile = File(project.rootDir, config.exportReport)
        reportFile.parentFile.mkdirs()
        reportFile.writeText(violations.joinToString(separator = "\n"))
    }
}
