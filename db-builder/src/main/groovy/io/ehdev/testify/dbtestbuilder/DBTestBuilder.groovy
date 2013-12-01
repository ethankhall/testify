package io.ehdev.testify.dbtestbuilder

import groovy.sql.Sql
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource

class DBTestBuilder {
    DataSource dataSource

    DBTestBuilder(JdbcTemplate jdbcTemplate) {
        this(jdbcTemplate.getDataSource())
    }

    DBTestBuilder(DataSource dataSource) {
        this.dataSource = dataSource
    }

    public TestCaseContainer processTestDatabase(String fileName) {
        def builder = new DBTestCase(new Sql(dataSource))
        runScript(fileName, builder)
        return builder.getTestCaseKeyResults()
    }

    public def runScript(String fileName, builder) {
        def testScript = new GroovyShell().parse(new File(fileName))
        setupMetaClass(testScript, builder)
        testScript.run()
    }

    public void setupMetaClass(Script testScript, builder) {
        setupMetaClassWithoutName(testScript, builder)

        setupMetaClassWithName(testScript, builder)
    }

    public void setupMetaClassWithName(Script testScript, builder) {
        testScript.metaClass.DBTestCase { name, closure ->
            builder.make(name, closure)
        }
    }

    public void setupMetaClassWithoutName(Script testScript, builder) {
        testScript.metaClass.DBTestCase { closure ->
            builder.make(closure)
        }
    }
}
