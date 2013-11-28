package io.ehdev.testify.dbtestbuilder
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import javax.sql.DataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData

import static org.mockito.Matchers.anyInt
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.MockitoAnnotations.initMocks

class MockedConnectionDBTestCaseTest {

    @Mock
    DataSource dataSource

    @Mock
    Connection connection

    @Mock
    PreparedStatement statement

    Integer idCount

    void setUpMockReturns() {
        initMocks(this)
        idCount = new Integer(1)
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement)
        when(statement.getGeneratedKeys()).thenAnswer(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocation) throws Throwable {
                return getResultSet()
            }
        })
    }

    public ResultSet getResultSet() {
        def resultSet = mock(ResultSet.class)
        def data = mock(ResultSetMetaData)

        when(data.getColumnCount()).thenReturn(1)
        when(resultSet.getMetaData()).thenReturn(data)
        when(resultSet.next()).thenReturn(true, false)
        when(resultSet.getObject(1)).thenReturn(idCount++)
        resultSet
    }

}
