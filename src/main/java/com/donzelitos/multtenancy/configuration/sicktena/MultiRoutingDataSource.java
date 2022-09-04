package com.donzelitos.multtenancy.configuration.sicktena;

import com.donzelitos.multtenancy.configuration.RequestContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        if (RequestContext.getCurrentInstance() != null) {
            return RequestContext.getCurrentInstance().getName();
        }
        return null;
    }
}