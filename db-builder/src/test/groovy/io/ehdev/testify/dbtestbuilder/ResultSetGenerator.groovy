package io.ehdev.testify.dbtestbuilder

import java.sql.ResultSet
import java.sql.ResultSetMetaData

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class ResultSetGenerator {

    synchronized static idCount = 0

    static public ResultSet getResultSet() {
        def resultSet = mock(ResultSet.class)
        def data = mock(ResultSetMetaData)

        when(data.getColumnCount()).thenReturn(1)
        when(resultSet.getMetaData()).thenReturn(data)
        when(resultSet.next()).thenReturn(true, false)
        when(resultSet.getObject(1)).thenReturn(idCount++)
        resultSet
    }

}
