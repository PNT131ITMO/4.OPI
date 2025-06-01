package com.example.linter

import org.gradle.api.Project

class SecretScanChecker(private val config: LinterExtension) {
    fun run(project: Project): List<String> {
        val results = mutableListOf<String>()
        if (!config.checkForSecrets) return results

        val patterns = listOf("AIza[0-9A-Za-z-_]{35}", "(?i)secret|password|token")
        val regexes = patterns.map { Regex(it) }

        project.rootDir.walkTopDown().filter { it.isFile && it.length() < 1024 * 100 }.forEach { file ->
            file.readLines().forEachIndexed { idx, line ->
                regexes.forEach { regex ->
                    if (regex.containsMatchIn(line)) {
                        results += "${file.path}:${idx + 1} Possible secret detected."
                    }
                }
            }
        }
        return results
    }
}
