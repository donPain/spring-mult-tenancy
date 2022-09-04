package com.donzelitos.multtenancy.configuration;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.BeansException;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class CustomDatabaseIdentifierNamingStrategy extends SpringPhysicalNamingStrategy implements ApplicationContextAware {

    private static final Pattern VALUE_PATTERN = Pattern.compile("^\\$\\{([\\w.]+)}$");
    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        environment = applicationContext.getBean(Environment.class);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply1(name, jdbcEnvironment);
    }

    private Identifier apply1(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        }

        String logicalText = name.getText();
        String physicalText = getPhysicalText(logicalText);
        if (physicalText != null) {
            log.info("Created database namespace [logicalName={}, physicalName={}]", logicalText, physicalText);
            return getIdentifier(physicalText, name.isQuoted(), jdbcEnvironment);
        }
        return null;
    }

    private String getPhysicalText(String logicalText) {
        String physicalText = null;
        Matcher matcher = VALUE_PATTERN.matcher(logicalText);
        if (matcher.matches()) {
            String propertyKey = matcher.group(1);
            physicalText = environment.getProperty(propertyKey);
            if (physicalText == null) {
                log.error("Environment property not found for key {}", propertyKey);
            }
        } else {
            log.error("Property key {} is not in pattern {}", logicalText, VALUE_PATTERN);
        }
        return physicalText;
    }
}
