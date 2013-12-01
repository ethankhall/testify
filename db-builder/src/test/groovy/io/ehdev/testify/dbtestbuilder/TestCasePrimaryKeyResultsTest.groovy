package io.ehdev.testify.dbtestbuilder
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class TestCasePrimaryKeyResultsTest {

    def testCase

    @BeforeTest
    public void setup() {
        testCase = new TestCasePrimaryKeyResults()
    }

    @Test
    public void testTableToIdMapping() throws Exception {
        createTableIds("table1")
        createTableIds("table2")
        assertThat(testCase.getPrimaryKeysForTable("table1")).hasSize(3)
        assertThat(testCase.getPrimaryKeysForTable("table2")).hasSize(3)
    }

    @Test
    public void testValuesInMap() throws Exception {
        createTableIds("table1")
        assertThat(testCase.getPrimaryKeysForTable('table1')).containsOnly(12, 13, 14)
    }


    private void createTableIds(String tableName) {
        testCase.addPrimaryKeyToTable(tableName, 12)
        testCase.addPrimaryKeyToTable(tableName, 13)
        testCase.addPrimaryKeyToTable(tableName, 14)
        testCase.addPrimaryKeyToTable(tableName, 12)
    }
}
