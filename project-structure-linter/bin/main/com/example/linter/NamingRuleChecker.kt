package com.example.linter

import org.gradle.api.Project
import java.io.File

class NamingRuleChecker(private val config: LinterExtension) {
    fun run(project: Project) : List<String> {
        val results = mutableListOf<String> ()
        val methodPattern = Regex(config.namingRules["method"] ?: "^[a-z][a-zA-Z0-9]*$") 
        val variablePattern = Regex(config.namingRules["variable"] ?: "^[a-z][a-zA-Z0-9]*$")
        val classPattern = Regex(config.namingRules["class"] ?: "^[A-Z][a-zA-Z0-9]*$")

        project.projectDir.walkTopDown().filter { it.extension == "java"}.forEach { file ->
            file.readLines().forEachIndexed { index, line ->
                Regex("class (\\w+)").find(line)?.groupValues?.getOrNull(1)?.let {
                    if (!classPattern.matches(it)) results += "${file.name}:${index + 1} Class '$it' violates naming convention"
                } 
                Regex("public | protected | private )?(static )?(final )?(\\w+) (\\w+)\\(").find(line)?.groupValues?.getOrNull(5)?.let {
                    if (!methodPattern.matches(it)) results += "${file.name}:${index + 1} Method '$it' violates naming convention"
                }
                Regex("(\\w+) (\\w+) ?= ?").find(line)?.groupValues?.getOrNull(2)?.let {
                    if (!variablePattern.matches(it)) results += "${file.name}:${index + 1} Variable '$it' violates naming convention"
                }
            }
        }
        return results
    }
}