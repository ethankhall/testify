package io.ehdev.testify.dbtestbuilder

class TestCaseContainer {
    Map<String, TestCasePrimaryKeyResults> testCases = [:]

    def addNewPrimaryKey(String currentTestName, String tableName, Integer rowResult){
        def testResults = getTestCaseResultsForTestName(currentTestName)
        testResults.addPrimaryKeyToTable(tableName, rowResult)
    }

    public TestCasePrimaryKeyResults getTestCaseResultsForTestName(String currentTestName) {
        if (null == testCases[currentTestName]) {
            testCases[currentTestName] = new TestCasePrimaryKeyResults()
        }
        testCases[currentTestName]
    }

    public List<Integer> getResultsForTableName(String tableName) {
        List<Integer> keys = []
        testCases.each { key, value ->
            keys.addAll(value.tableToPrimaryKeyMap[tableName])
        }
        return keys
    }
}
