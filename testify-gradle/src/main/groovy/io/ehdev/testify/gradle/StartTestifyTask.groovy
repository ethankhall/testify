package io.ehdev.testify.gradle
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.testing.Test
import org.h2.Driver
import org.h2.tools.Server

import java.sql.Connection

@Slf4j
class StartTestifyTask extends DefaultTask{

    @TaskAction
    void start() {
        log.debug("Starting task")

        def databaseName = project.testify.databaseName;
        def scripts = project.testify.scripts;
        def filterTestTasks = project.testify.excludeTestTasks;

        databaseName = databaseName ?: Long.toString(System.currentTimeMillis())

        // open the in-memory database within a VM
        Connection conn = new Driver().connect("jdbc:h2:mem:$databaseName;MODE=MySQL", null)

        scripts.each { String it ->
            conn.createStatement().execute(new File(it).text);
        }

        Server.createTcpServer().start();
        log.debug("Server now up an running, please use jdbc:h2:localhost:mem:$databaseName")

        project.tasks.withType(Test).each { task ->
            if(filterTestTasks.isEmpty() || filterTestTasks.contains(task.name) ) {
                task.allJvmArgs += "-D__testifyDBName=$databaseName"
                log.debug("Adding parameter to the JVM: -D__testifyDBName=$databaseName")
            }
        }
    }
}
