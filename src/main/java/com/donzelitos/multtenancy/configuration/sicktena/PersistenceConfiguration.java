package com.donzelitos.multtenancy.configuration.sicktena;

import com.solinftec.preregistrationapi.configuration.exception.SolinftecDefaultException;
import com.solinftec.preregistrationapi.service.owner.OwnerService;
import com.solinftec.preregistrationapi.utils.CryptographyTripleDES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.crypto.NoSuchPaddingException;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableSpringDataWebSupport
public class PersistenceConfiguration {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private FactoryConnection factoryConnection;
    @Value("${default.connection.username}")
    private String defaultConnectionUser;

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
        ownerService.getAllInstances().forEach(instance -> {
            try {
                var dataSource = factoryConnection.getConnection(instance);
                targetDataSources.put(instance.getName(), dataSource);
            } catch (SolinftecDefaultException e) {
                e.printStackTrace();
            }
        });
        return targetDataSources;
    }

    @Bean
    CryptographyTripleDES cryptographyUtils() throws InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return CryptographyTripleDES.newInstance();
    }
}

