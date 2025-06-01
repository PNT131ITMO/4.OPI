package com.example.linter

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class ProjectStructureLinterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("projectLinter", LinterExtension::class.java)

        project.afterEvaluate {
            val violations = mutableListOf<String>()

            violations += NamingRuleChecker(extension).run(project)
            violations += DependencyRuleChecker(extension).run(project)
            violations += DirectoryStructureChecker(extension).run(project)
            violations += SecretScanChecker(extension).run(project)
            violations += TestCoverageChecker(extension).run(project)
            violations += ConfigFileChecker(extension).run(project)

            LintReport(extension).export(project, violations)


            if (extension.failOnViolation && violations.isNotEmpty()) {
                throw RuntimeException("Project structure violations found. See report for details.")
            }
        }
    }
}