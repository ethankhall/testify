package io.ehdev.testify.dbtestbuilder
import groovy.sql.Sql
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class DBTestCaseTest {

    @Mock
    Sql connection
    private DBTestCase testCase

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this)
        testCase = new DBTestCase(connection)
    }

    @Test
    public void testTableToIdMapping() throws Exception {
        createTableIds("table1")
        createTableIds("table2")
        assertThat(testCase.getTableToIdMap()['table1']).hasSize(3)
        assertThat(testCase.getTableToIdMap()['table2']).hasSize(3)
    }

    @Test
    public void testValuesInMap() throws Exception {
        createTableIds("table1")
        assertThat(testCase.getTableToIdMap()['table1']).containsOnly(12, 13, 14)
    }

    private void createTableIds(String tableName) {
        testCase.setIdForTable(tableName, 12)
        testCase.setIdForTable(tableName, 13)
        testCase.setIdForTable(tableName, 14)
        testCase.setIdForTable(tableName, 12)
    }
}
