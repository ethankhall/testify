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

    public Map<String, List<Integer>> processTestDatabase(String fileName) {
        def builder = new DBTestCase(new Sql(dataSource))
        setupScriptToRun(fileName, builder)
        return builder.getTableToIdMap()
    }

    public Script setupScriptToRun(String fileName, builder) {
        def testScript = new GroovyShell().parse(new File(fileName))
        testScript.metaClass.DBTestCase {
            builder.make(it)
        }
        testScript.run()
        testScript
    }
}
