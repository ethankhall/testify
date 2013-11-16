package io.ehdev.gradle.testify

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

@Slf4j
class TestifyDatabasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.create("testify", TestifyPluginExtension)

        StartTestifyTask task = project.tasks.create('startTestify', StartTestifyTask)
        task.description = "Start an H2 database locally for the life of gradle"
        task.group = "testify"

        setTestsWithDBInfo(project)
    }

    void setTestsWithDBInfo(final Project project) {
        project.tasks.withType(Test).whenTaskAdded { Test task ->
            log.debug("Task Added")
            log.debug(project.testify.databaseName)
            project.gradle.taskGraph.whenReady {
                log.debug("Graph Ready")
                if(project.testify.filterTestTasks.isEmpty() || project.testify.contains(task.name) ) {
                    task.allJvmArgs += "-D__testifyDBName=$project.testify.databaseName"
                    log.debug("Adding parameter to the JVM: -D__testifyDBName=$project.testify.databaseName")
                }
            }
        }
    }
}
