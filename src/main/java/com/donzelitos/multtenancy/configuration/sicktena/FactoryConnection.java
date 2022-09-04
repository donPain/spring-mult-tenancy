package com.donzelitos.multtenancy.configuration.sicktena;

import com.donzelitos.multtenancy.configuration.exception.SolinftecDefaultException;
import com.donzelitos.multtenancy.model.Instance;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class FactoryConnection {

    private static final String OWNER_NOT_FOUND = "Owner not found";
    private static Logger logger = LoggerFactory.getLogger(FactoryConnection.class);
    @Value("${default.connection.url}")
    private String defaultConnectionUrl;
    @Value("${default.connection.username}")
    private String defaultConnectionUser;
    @Value("${default.connection.password}")
    private String defaultConnectionPassword;


    public DataSource getConnection(String url, String user, String password) {

        try {
            return new DriverManagerDataSource(url, user, password);
        } catch (Exception e) {
            return null;
        }
    }

    public DataSource getConnection(Instance instance) throws SolinftecDefaultException {
        logger.info("{} criado", instance.getName());
        var config = new HikariConfig();
        config.setJdbcUrl(instance.getUrl());
        config.setUsername(defaultConnectionUser);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setPassword(defaultConnectionPassword);
        config.setInitializationFailTimeout(0);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    public DataSource getDefaultConnection() {
        var config = new HikariConfig();
        config.setJdbcUrl(defaultConnectionUrl);
        config.setUsername(defaultConnectionUser);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setPassword(defaultConnectionPassword);
        config.setInitializationFailTimeout(0);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

}
