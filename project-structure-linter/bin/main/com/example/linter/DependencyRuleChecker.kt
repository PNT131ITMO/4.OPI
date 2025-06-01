package com.example.linter

import org.gradle.api.Project

class DependencyRuleChecker(private val config: LinterExtension) {
    fun run(project: Project): List<String> {
        val results = mutableListOf<String>()
        config.allowedDependencies.forEach { (moduleName, allowed) ->
            val module = project.rootProject.subprojects.find { it.name == moduleName } ?: return@forEach
            module.configurations.matching { it.name == "implementation" || it.name == "api" }.forEach { cfg ->
            cfg.dependencies.forEach { dep ->
                val depName = dep.name
                if (depName in config.allowedDependencies.keys && depName !in allowed) {
                results += "$moduleName -> $depName is not allowed"
                }
            }
            }
        }
        return results
    }
}