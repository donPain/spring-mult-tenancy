package com.donzelitos.multtenancy.configuration.sicktena;

import com.donzelitos.multtenancy.configuration.RequestContext;
import com.donzelitos.multtenancy.configuration.exception.SolinftecDefaultException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
@Slf4j
public class ConnectionAspect {

    @Around("execution(java.sql.Connection javax.sql.DataSource.getConnection(..))")
    public Connection prepare(ProceedingJoinPoint pjp) throws Exception {
        if (pjp.getTarget() != null && pjp.getTarget() instanceof MultiRoutingDataSource ds) {
            RequestContext.owner();
            try  {
                Connection conn = ds.getResolvedDataSources().get(RequestContext.getCurrentInstance().getName()).getConnection();
                if (RequestContext.owner() != null) {
                    conn.setSchema("PRE_CADASTRO_DEV");
                }
                return conn;
            } catch (Exception e) {
                ds.getResolvedDataSources().get(RequestContext.getCurrentInstance()).getConnection().close();
                log.error("Error on get connection");
                throw new SolinftecDefaultException("Fail to set schema.");
            }
        }
        return null;
    }
}
