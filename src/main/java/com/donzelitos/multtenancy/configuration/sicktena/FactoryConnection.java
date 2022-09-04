package com.donzelitos.multtenancy.configuration.sicktena;

import com.solinftec.preregistrationapi.configuration.exception.SolinftecDefaultException;
import com.solinftec.preregistrationapi.model.Instance;
import com.solinftec.preregistrationapi.service.owner.DbOwner;
import com.solinftec.preregistrationapi.service.owner.OwnerService;
import com.solinftec.preregistrationapi.utils.CryptographyTripleDES;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.DecodingException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class FactoryConnection {

    private static final String OWNER_NOT_FOUND = "Owner not found";
    private static Logger logger = LoggerFactory.getLogger(FactoryConnection.class);
    private final OwnerService ownerService;
    @Value("${default.connection.url}")
    private String defaultConnectionUrl;
    @Value("${default.connection.username}")
    private String defaultConnectionUser;
    @Value("${default.connection.password}")
    private String defaultConnectionPassword;

    public FactoryConnection(final OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public DataSource getConnection(String url, String user, String password) {

        try {
            return new DriverManagerDataSource(url, user, password);
        } catch (Exception e) {
            return null;
        }
    }

    public String getURLConnectionByOwner(String owner) throws SolinftecDefaultException {

        var dbOwner = ownerService.getOwner(owner);

        if ("yes".equals(dbOwner.hasServiceName))
            return getURLConnectionByOwnerServiceName(dbOwner);
        return getURLConnectionByOwnerSID(dbOwner);
    }

    private String getURLConnectionByOwnerSID(DbOwner dbOwner) throws SolinftecDefaultException {
        try {
            var sb = new StringBuilder();
            sb.append("jdbc:oracle:thin:@");
            sb.append(dbOwner.prod_ip);
            sb.append(":");
            sb.append(dbOwner.prod_port);
            sb.append(":");
            sb.append(dbOwner.instance);
            return sb.toString();
        } catch (Exception e) {
            throw new SolinftecDefaultException(OWNER_NOT_FOUND);
        }
    }

    private String getURLConnectionByOwnerServiceName(DbOwner dbOwner) throws SolinftecDefaultException {
        try {
            return String.format("jdbc:oracle:thin:@(description=(address=(host=%s)(protocol=tcp)(port=%s))(connect_data=(sid=%s)(server=DEDICATED)))", dbOwner.prod_ip, dbOwner.prod_port, dbOwner.instance);
        } catch (Exception e) {
            throw new SolinftecDefaultException(OWNER_NOT_FOUND);
        }
    }

    public String getPasswordByOwner(String owner) throws SolinftecDefaultException {

        var dbOwner = ownerService.getOwner(owner);

        if (dbOwner != null) {
            return decriptPassword(dbOwner.password);
        } else
            throw new SolinftecDefaultException(OWNER_NOT_FOUND + owner);
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
        config.setPassword(decriptPassword(defaultConnectionPassword));
        config.setInitializationFailTimeout(0);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    private String decriptPassword(String password) {
        try {
            var decryptor = new CryptographyTripleDES();
            return decryptor.decrypt(password);
        } catch (Exception e) {
            throw new DecodingException(e.getMessage());
        }
    }
}
