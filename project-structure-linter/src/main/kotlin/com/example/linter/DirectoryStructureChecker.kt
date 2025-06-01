package com.example.linter

import org.gradle.api.Project
import java.io.File

class DirectoryStructureChecker(private val config: LinterExtension) {
    fun run(project: Project): List<String> {
        val results = mutableListOf<String>()
        if (!config.enforceDirectoryStructure) return results

        project.subprojects.forEach {sub ->
            val mainSrc = File(sub.projectDir, "src/main/java")
            if (!mainSrc.exists()) {
                results += "${sub.name}: Missing src/main/java directory"
            } 
        }
        return results
    }
}