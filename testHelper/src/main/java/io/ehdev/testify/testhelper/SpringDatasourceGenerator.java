package io.ehdev.testify.testhelper;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDataSourceGenerator {

    private static final String URL = "jdbc:h2:tcp://localhost/mem:%s;MODE=MySQL;IFEXISTS=TRUE;DB_CLOSE_ON_EXIT=FALSE";

    @Bean
    public BasicDataSource getTestDatasource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl(String.format(URL, System.getProperty("__testifyDBName")));
        return basicDataSource;
    }

}
