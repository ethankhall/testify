package com.example.testify;

import io.ehdev.testify.dbtestbuilder.DBTestBuilder;
import io.ehdev.testify.dbtestbuilder.TestCaseContainer;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeTest;

import static org.fest.assertions.Assertions.assertThat;

public class PopulateDatabaseFromFileTest {

    @BeforeTest
    public void setup() throws Exception {
        BasicDataSource dataSource = createDataSource();
        createTables(dataSource);
        DBTestBuilder builder = new DBTestBuilder(dataSource);
        TestCaseContainer testCaseContainer = builder.processTestDatabase(getClass().getResource("/example_using_named_value.db").getPath());
        assertThat(testCaseContainer.getResultsForTableName("table1")).hasSize(1);
        assertThat(testCaseContainer.getResultsForTableName("table2")).hasSize(1);
    }


    public BasicDataSource createDataSource() throws Exception {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:populate;MODE=MySQL;");
        return basicDataSource;
    }

    private void createTables(BasicDataSource dataSource) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.execute("create table table1 (id int NOT NULL primary key AUTO_INCREMENT, field1 varchar(255), field2 varchar(255), field3 varchar(255))");
        template.execute("create table table2 (id int NOT NULL primary key AUTO_INCREMENT, field1 varchar(255), field2 varchar(255), field3 varchar(255), refId int,  foreign key (refId) references table1(id) )");
    }
}
