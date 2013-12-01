package io.ehdev.testify.dbtestbuilder

class TestCasePrimaryKeyResults {
    Map<String, Set<Integer>> tableToPrimaryKeyMap = [:]

    def addPrimaryKeyToTable(String tableName, Integer primaryKey) {
        Set<Integer> keys = getPrimaryKeysForTable(tableName)
        keys << primaryKey
    }

    Set<Integer> getPrimaryKeysForTable(String tableName) {
        if(null == tableToPrimaryKeyMap[tableName]) {
            tableToPrimaryKeyMap[tableName] = new HashSet<Integer>()
        }
        tableToPrimaryKeyMap[tableName]
    }
}
