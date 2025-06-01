package com.example.linter

import org.gradle.api.Project

class ConfigFileChecker(private val config: LinterExtension) {
    fun run(project: Project): List<String> {
        val requiredFiles = listOf(".editorconfig", ".gitignore", "README.md")
        val missing = requiredFiles.filterNot { project.rootDir.resolve(it).exists() }
        return missing.map { "Missing required config file: $it" }
    }
}