package io.ehdev.testify.dbtestbuilder

import org.apache.commons.dbcp.BasicDataSource
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.springframework.jdbc.core.JdbcTemplate
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import javax.sql.DataSource
import java.sql.*

import static org.mockito.Matchers.anyInt
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*
import static org.mockito.MockitoAnnotations.initMocks

class DBTestBuilderTest {

    def builder

    @Mock
    DataSource dataSource

    @Mock
    Connection connection

    @Mock
    PreparedStatement statement

    int idCount = 1

    @BeforeTest
    public void setup() {
        initMocks(this)
        setUpMockReturns()
    }

    private void setUpMockReturns() {
        when(dataSource.getConnection()).thenReturn(connection)
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement)
        when(statement.getGeneratedKeys()).thenAnswer(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocation) throws Throwable {
                return getResultSet()
            }
        })
    }

    private ResultSet getResultSet() {
        def resultSet = mock(ResultSet.class)
        def data = mock(ResultSetMetaData)

        when(data.getColumnCount()).thenReturn(1)
        when(resultSet.getMetaData()).thenReturn(data)
        when(resultSet.next()).thenReturn(true, false)
        when(resultSet.getObject(1)).thenReturn(idCount++)
        resultSet
    }

    @Test
    public void testReadingFromJsonScript() throws Exception {
        createDBTestBuilder("db_example1.db", dataSource)
        verify(statement).setObject(4,1)
        verify(statement).setObject(4,2)
        verify(statement).setObject(4,3)
    }

    @Test
    public void testWithH2DB() throws Exception {
        JdbcTemplate template = createJdbcTemplate()
        template.execute("create table table1 (id int NOT NULL primary key AUTO_INCREMENT, field1 varchar(255), field2 varchar(255), field3 varchar(255))")
        template.execute("create table table2 (id int NOT NULL primary key AUTO_INCREMENT, field1 varchar(255), field2 varchar(255), field3 varchar(255), refId int,  foreign key (refId) references table1(id) )")
        createDBTestBuilder("db_example1.db", template)

    }

    private JdbcTemplate createJdbcTemplate() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:db1;MODE=MySQL;");
        def template = new JdbcTemplate(basicDataSource)
        template
    }

    def createDBTestBuilder(String fileName, DataSource dataSource) {
        def resource = getClass().getResource("/$fileName")
        builder = new DBTestBuilder(resource.getFile(), dataSource)
    }

    def createDBTestBuilder(String fileName, JdbcTemplate jdbcTemplate) {
        def resource = getClass().getResource("/$fileName")
        builder = new DBTestBuilder(resource.getFile(), jdbcTemplate)
    }
}
