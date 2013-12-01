package io.ehdev.testify.dbtestbuilder

import groovy.sql.Sql
import groovy.util.logging.Slf4j

@Slf4j
/**
 * This class is thread safe, if and only if the test creates a new object every time.
 */
class DBTestCase {

    final Sql connection;
    def testCaseKeyResults
    def currentTestName

    DBTestCase(Sql connection){
        this.connection = connection
        this.testCaseKeyResults = new TestCaseContainer()
    }

    def make(name, closure) {
        if(!name.trim()) {
            log.debug("Setting up test case named $name")
        }
        closure.delegate = this
        closure()
        currentTestName = name
    }

    def make(closure) {
        make("DEFAULT_TEST", closure)
    }

    def methodMissing(String tableName, args){
        Integer rowResult = insertIntoDatabase(tableName, args)
        testCaseKeyResults.addNewPrimaryKey(currentTestName, tableName, rowResult)
        //updatePrimaryKeyList(tableName, rowResult)
        return rowResult
    }

    public Object insertIntoDatabase(String name, args) {
        Map fields = getFieldMap(args)
        String insertStatement = createInsertString(fields, name)
        log.debug("Writing to table $name with parameters {}", fields)
        def rowResult = executeTransactionToDatabase(fields, insertStatement)
        rowResult
    }

    public Object executeTransactionToDatabase(Map fields, String insertStatement) {
        try {
            return connection.executeInsert(fields, insertStatement)[0][0]
        } catch (ex) {
            throw new InvalidDatabaseOperationException(ex)
        }
    }

    private GString createInsertString(fields, String name) {
        String values, params

        (values, params) = getValueString(fields)

        "insert into $name ($values) values ($params)";
    }

    private static def getFieldMap(args) {
        args[0]
    }

    private def getValueString(fields) {
        def values = "";
        def params = ""
        def columnNames = fields.keySet()
        columnNames.eachWithIndex { it, i ->
            def deliminator = getDeliminator(i, columnNames.size())
            values += it + deliminator
            params += ":$it" + deliminator
        }
        [values, params]
    }

    private static String getDeliminator(i, size) {
        (size != i + 1) ? ", " : ""
    }
}
