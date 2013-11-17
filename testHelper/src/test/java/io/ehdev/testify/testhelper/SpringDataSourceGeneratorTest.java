package io.ehdev.testify.testhelper;

import org.apache.commons.dbcp.BasicDataSource;
import org.h2.Driver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;

import static org.fest.assertions.Assertions.assertThat;

public class SpringDataSourceGeneratorTest {

    ClassPathXmlApplicationContext appContext;

    @BeforeTest
    public void setup() throws Exception {
        Connection conn = new Driver().connect("jdbc:h2:mem:TESTDATABASE1;MODE=MySQL", null);
        System.setProperty("__testifyDBName", "TESTDATABASE1");

        appContext = new ClassPathXmlApplicationContext(new String[] {"test-datasource.xml"});
    }

    @Test
    public void testUsingConfiguration() throws Exception {
        BasicDataSource bean = appContext.getBean(BasicDataSource.class);
        assertThat(bean).isNotNull();
    }
}
