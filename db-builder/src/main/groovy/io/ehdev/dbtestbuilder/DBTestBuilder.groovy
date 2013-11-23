package io.ehdev.dbtestbuilder

import groovy.sql.Sql
import org.springframework.jdbc.core.JdbcTemplate

class DBTestBuilder {
    def testScript

    DBTestBuilder(String fileName, JdbcTemplate jdbcTemplate) {
        DBTestCase.setConnection(new Sql(jdbcTemplate.getDataSource()))
        testScript = new GroovyShell().parse(new File(fileName))
        testScript.run()
    }
}
