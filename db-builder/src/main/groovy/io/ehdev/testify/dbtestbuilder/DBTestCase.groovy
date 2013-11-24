package io.ehdev.testify.dbtestbuilder

import groovy.sql.Sql

class DBTestCase {

    final Sql connection;

    DBTestCase(Sql connection){
        this.connection = connection
    }

    def make(closure) {
        closure.delegate = this
        closure()
    }

    def methodMissing(String name, args){
        def fields = getFieldMap(args)
        String insertStatement = createInsertString(fields, name)
        println(insertStatement)

        def rowResult = connection.executeInsert(fields, insertStatement)[0][0]
        println(fields)
        rowResult
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
