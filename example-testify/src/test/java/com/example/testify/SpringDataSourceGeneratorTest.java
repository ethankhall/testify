package com.example.testify;

import org.apache.commons.dbcp.BasicDataSource;
import org.fest.assertions.MapAssert;
import org.h2.Driver;
import org.h2.tools.Server;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.ehdev.testify.testhelper.SpringDataSourceGenerator;

import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class SpringDataSourceGeneratorTest {

    @Test
    public void testUsingXML() throws Exception {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {"test-datasource.xml"});
        testJdbcTemplate(appContext);
    }

    private void testJdbcTemplate(AbstractRefreshableConfigApplicationContext appContext) {
        BasicDataSource dataSource = appContext.getBean(BasicDataSource.class);
        assertThat(dataSource).isNotNull();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("create table mytable (id integer, name varchar(100))");
        jdbcTemplate.update("insert into mytable values (1, 'foo')");
        jdbcTemplate.update("insert into mytable values (2, 'bar')");
        List<Map<String,Object>> maps = jdbcTemplate.queryForList("select * from mytable");
        assertThat(maps).hasSize(2);
        assertThat(maps.get(0)).includes( MapAssert.entry("id", 1), MapAssert.entry("name", "foo") );
        assertThat(maps.get(1)).includes( MapAssert.entry("id", 2), MapAssert.entry("name", "bar") );
        jdbcTemplate.execute("drop table mytable");
    }

    @Test
    public void testUsingConfiguration() throws Exception {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringDataSourceGenerator.class);
        context.refresh();
        testJdbcTemplate(context);
    }
}
