package io.ehdev.testify.dbtestbuilder
import groovy.sql.Sql
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.MockitoAnnotations.initMocks

class LargeDBTestCaseTest extends MockedConnectionDBTestCaseTest{

    def testCase

    @BeforeTest
    public void setup(){
        initMocks(this)
        setUpMockReturns()
        testCase = new DBTestCase(new Sql(connection))
    }

    @Test
    public void testHavingADescriptionOfTheData() throws Exception {
        testCase.make("this is an example with a description") {
            table2( field1: 1, field2: 2, field3: 3, refId: table1( field1: 1, field2: 2, field3: 3))
        }

    }

}
