package io.ehdev.testify.dbtestbuilder
import org.apache.commons.dbcp.BasicDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import javax.sql.DataSource

import static org.fest.assertions.Assertions.assertThat
import static org.mockito.Mockito.verify
import static org.mockito.MockitoAnnotations.initMocks

class DBTestBuilderTest extends MockedConnectionDBTestCaseTest{

    def builder


    @BeforeTest
    public void setup() {
        initMocks(this)
        setUpMockReturns()
    }

    @Test
    public void testReadingFromJsonScript() throws Exception {
        def tableResults = createDBTestBuilder("db_example1.db", dataSource)
        verify(statement).setObject(4,1)
        verify(statement).setObject(4,2)
        verify(statement).setObject(4,3)
        assertThat(tableResults.getResultsForTableName("table1")).hasSize(3)
        assertThat(tableResults.getResultsForTableName("table2")).hasSize(3)
    }

    @Test
    public void testWithH2DB() throws Exception {
        JdbcTemplate template = createJdbcTemplate("db1")
        createTableForTests(template)
        def tableResults = createDBTestBuilder("db_example1.db", template)
        assertThat(tableResults.getResultsForTableName("table1")).hasSize(3)
        assertThat(tableResults.getResultsForTableName("table2")).hasSize(3)
    }

    @Test(expectedExceptions = InvalidDatabaseOperationException.class)
    public void testFailingToOnNonExistentTableInsertId() throws Exception {
        JdbcTemplate template = createJdbcTemplate("db2")
        createTableForTests(template)
        createDBTestBuilder("db_example2.db", template)
    }

    @Test
    public void testWithH2AndNamedParameter() throws Exception {
        JdbcTemplate template = createJdbcTemplate("db3")
        createTableForTests(template)
        def tableResults = createDBTestBuilder("example_using_named_value.db", template)
        assertThat(tableResults.getResultsForTableName("table1")).hasSize(1)
        assertThat(tableResults.getResultsForTableName("table2")).hasSize(1)
    }

    public void createTableForTests(JdbcTemplate template) {
        template.execute("create table table1 (id int NOT NULL primary key AUTO_INCREMENT, field1 varchar(255), field2 varchar(255), field3 varchar(255))")
        template.execute("create table table2 (id int NOT NULL primary key AUTO_INCREMENT, field1 varchar(255), field2 varchar(255), field3 varchar(255), refId int,  foreign key (refId) references table1(id) )")
    }

    private JdbcTemplate createJdbcTemplate(String name) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:$name;MODE=MySQL;");
        def template = new JdbcTemplate(basicDataSource)
        template
    }

    def createDBTestBuilder(String fileName, DataSource dataSource) {
        def resource = getClass().getResource("/$fileName")
        builder = new DBTestBuilder(dataSource)
        builder.processTestDatabase(resource.getFile())
    }

    def createDBTestBuilder(String fileName, JdbcTemplate jdbcTemplate) {
        def resource = getClass().getResource("/$fileName")
        builder = new DBTestBuilder(jdbcTemplate)
        builder.processTestDatabase(resource.getFile())
    }
}
