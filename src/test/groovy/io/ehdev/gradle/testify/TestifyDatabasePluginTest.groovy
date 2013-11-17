package io.ehdev.gradle.testify
import groovy.sql.Sql
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class TestifyDatabasePluginTest {

    @Test
    public void testAddingTaskToProject() throws Exception {
        Project project = createBaseProject("test")

        project.testify.scripts = [ 'src/test/resources/001_first_script.sql', 'src/test/resources/002_second_script.sql' ]

        assertThat(project.tasks.startTestify).isInstanceOf(StartTestifyTask.class)

        project.tasks.startTestify.start()

        def db = [url:'jdbc:h2:tcp://localhost/mem:test',  driver:'org.h2.Driver']
        def sql = Sql.newInstance(db.url, db.driver)
        assertThat(sql.firstRow("SELECT count(*) as numberOfRows FROM some_table").numberOfRows).isEqualTo(2)
        assertThat(sql.firstRow("SELECT count(*) as numberOfRows FROM second_table").numberOfRows).isEqualTo(2)
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

    private Project createBaseProject(String databaseName) {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: TestifyDatabasePlugin
        project.apply plugin: "java"
        project.testify.databaseName = databaseName
        project
    }
}
