package io.ehdev.testify.dbtestbuilder

import groovy.sql.Sql
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource

class DBTestBuilder {
    def testScript

    DBTestBuilder(String fileName, JdbcTemplate jdbcTemplate) {
        this(fileName, jdbcTemplate.getDataSource())
    }

    DBTestBuilder(String fileName, DataSource dataSource) {
        def builder = new DBTestCase(new Sql(dataSource))
        testScript = new GroovyShell().parse(new File(fileName))
        testScript.metaClass.DBTestCase {
            builder.make(it)
        }
        testScript.run()
    }
}
