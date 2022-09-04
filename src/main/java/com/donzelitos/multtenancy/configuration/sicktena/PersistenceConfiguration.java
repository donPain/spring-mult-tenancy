package com.donzelitos.multtenancy.configuration.sicktena;

import com.donzelitos.multtenancy.configuration.exception.SolinftecDefaultException;
import com.donzelitos.multtenancy.model.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableSpringDataWebSupport
public class PersistenceConfiguration {

    @Autowired
    private FactoryConnection factoryConnection;
    @Value("${default.connection.username}")
    private String defaultConnectionUser;
    @Value("${instancias.primary}")
    private String primaryInstanceUrl;
    @Value("${instancias.secondary}")
    private String secondaryInstanceUrl;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = getInstances();
        Object defaultDataSource = targetDataSources.get(targetDataSources.entrySet().iterator().next().getKey());
        var multiRoutingDataSource = new MultiRoutingDataSource();
        multiRoutingDataSource.setDefaultTargetDataSource(defaultDataSource);
        multiRoutingDataSource.setTargetDataSources(targetDataSources);
        multiRoutingDataSource.afterPropertiesSet();
        return multiRoutingDataSource;
    }

    private Map<Object, Object> getInstances() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        var instance1 = new Instance("PRIMARY", primaryInstanceUrl);
        var instance2 = new Instance("SECONDARY", secondaryInstanceUrl);
        var instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        instances.forEach(instance -> {
            try {
                var dataSource = factoryConnection.getConnection(instance);
                targetDataSources.put(instance.getName(), dataSource);
            } catch (SolinftecDefaultException e) {
                e.printStackTrace();
            }
        });
        return targetDataSources;
    }
}

