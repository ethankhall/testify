package io.ehdev.gradle.testify
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.h2.Driver
import org.h2.tools.Server

import java.sql.Connection

@Slf4j
class StartTestifyTask extends DefaultTask{

    StartTestifyTask(){
        outputs.upToDateWhen { false }
    }

    @Input
    def databaseName

    @Input
    def filterTestTasks = []

    @InputFiles
    def scripts = []

    @TaskAction
    void start() {
        log.debug("Starting task")

        databaseName = project.testify.databaseName;
        scripts = project.testify.scripts;
        filterTestTasks = project.testify.filterTestTasks;

        // open the in-memory database within a VM
        Connection conn = new Driver().connect("jdbc:h2:mem:$databaseName;MODE=MySQL", null)

        scripts.each { String it ->
            conn.createStatement().execute(new File(it).text);
        }

        Server.createTcpServer().start();
        log.debug("Server now up an running, please use jdbc:h2:localhost:mem:$databaseName")
    }
}
