package io.ehdev.testify.gradle
import groovy.sql.Sql
import groovy.util.logging.Log4j
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.h2.jdbc.JdbcSQLException
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

@Log4j
class TestifyDatabasePluginTest {

    @Test
    public void testAddingTaskToProject() throws Exception {
        Project project = createBaseProject("test")
        def script1 =  getClass().getResource("/001_first_script.sql").toURI().getPath()
        def script2 =  getClass().getResource("/002_second_script.sql").toURI().getPath()

        project.testify.scripts = [ script1, script2 ]

        assertThat(project.tasks.startTestify).isInstanceOf(StartTestifyTask.class)

        project.tasks.startTestify.start()

        def db = [url:'jdbc:h2:tcp://localhost/mem:test;IFEXISTS=TRUE',  driver:'org.h2.Driver']
        def sql = Sql.newInstance(db.url, db.driver)
        assertThat(sql.firstRow("SELECT count(*) as numberOfRows FROM some_table").numberOfRows).isEqualTo(2)
        assertThat(sql.firstRow("SELECT count(*) as numberOfRows FROM second_table").numberOfRows).isEqualTo(2)
    }

    @Test(expectedExceptions = JdbcSQLException.class)
    public void testConnectionWillFailIfNotStarted() throws Exception {
        def db = [url:'jdbc:h2:tcp://localhost/mem:DATABASE_NOT_YET_CREATED;IFEXISTS=TRUE',  driver:'org.h2.Driver']
        def sql = Sql.newInstance(db.url, db.driver)
    }

    @Test
    public void testAddingJVMOptions() throws Exception {
        Project project = createBaseProject("test1")

        project.tasks.startTestify.start()

        def testTask = project.getTasks().getByName("test")
        assertThat(testTask).isInstanceOf(org.gradle.api.tasks.testing.Test)
        assertThat(((org.gradle.api.tasks.testing.Test)testTask).allJvmArgs).contains("-D__testifyDBName=test1")
    }

    @Test
    public void testSkippingTasksToAddJVMArg() throws Exception {
        Project project = createBaseProject("test2")
        project.testify.excludeTestTasks = 'test'

        def testTask = project.getTasks().getByName("test")
        assertThat(((org.gradle.api.tasks.testing.Test)testTask).getAllJvmArgs()).hasSize(2)
    }

    @Test
    public void testAutomaticDatabaseName() throws Exception {
        Project project = createBaseProject(null)
        project.tasks.startTestify.start()

        def testTask = project.getTasks().getByName("test")
        assertThat(testTask).isInstanceOf(org.gradle.api.tasks.testing.Test)

        String args = ((org.gradle.api.tasks.testing.Test)testTask).allJvmArgs.find { it =~ /-D__test.*/ }
        assertThat(Long.parseLong(args.split("=")[1])).isNotNull()
    }

    private static Project createBaseProject(String databaseName) {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: TestifyDatabasePlugin
        project.apply plugin: "java"
        project.testify.databaseName = databaseName
        project
    }
}
