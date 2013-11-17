package io.ehdev.testify.gradle

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

@Slf4j
class TestifyDatabasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.create("testify", TestifyPluginExtension)

        project.task(type: StartTestifyTask, 'startTestify') {
            description = "Start an H2 database locally for the life of the gradle instance"
            group = "testify"
        }

//        setTestsWithDBInfo(project)
    }

    void setTestsWithDBInfo(final Project project) {

        project.tasks.withType(Test).e.whenTaskAdded { Test task ->
            log.debug("Task Added")
            log.debug(task.project.getExtensions().getByName("testify").databaseName)
            project.gradle.taskGraph.whenReady {
                log.debug("Graph Ready")
                if(project.testify.excludeTestTasks.isEmpty() || project.testify.excludeTestTasks.contains(task.name) ) {
                    task.allJvmArgs += "-D__testifyDBName=$project.testify.databaseName"
                    log.debug("Adding parameter to the JVM: -D__testifyDBName=$project.testify.databaseName")
                }
            }
        }
    }
}
