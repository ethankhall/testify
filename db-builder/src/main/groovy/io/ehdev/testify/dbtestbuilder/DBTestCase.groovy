package io.ehdev.testify.dbtestbuilder

import groovy.sql.Sql
import groovy.util.logging.Slf4j

@Slf4j
class DBTestCase {

    final Sql connection;
    def tableToIdMap = [:]

    DBTestCase(Sql connection){
        this.connection = connection
    }

    def make(name, closure) {
        if(!name.trim()) {
            log.debug("Setting up test case named $name")
        }
        closure.delegate = this
        closure()
    }

    def make(closure) {
        make("", closure)
    }

    def methodMissing(String name, args){
        Integer rowResult = insertIntoDatabase(args, name)
        setIdForTable(name, rowResult)
        return rowResult
    }

    public Object insertIntoDatabase(args, String name) {
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

    void setIdForTable(String name, def ids) {
        def idsForName = tableToIdMap[name]
        if(null == idsForName) {
            idsForName = new HashSet<Integer>();
        }
        idsForName << ids
        tableToIdMap[name] = idsForName
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
