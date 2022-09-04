package com.donzelitos.multtenancy.configuration;

import com.donzelitos.multtenancy.model.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RequestContext {


    private static final ThreadLocal<Owner> contextOwner = new ThreadLocal<>();
    private static final ThreadLocal<Instance> contextInstance = new ThreadLocal<>();
    private static final ThreadLocal<String> testOwnerString = new ThreadLocal<>();
    private static final ThreadLocal<Long> localeThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    private static String defaultOwnerName;
    private static String defaultInstance;
    private static String defaultUrl;

    @Value("${default.connection.username}")
    private String defaultConnectionUser;

    @Value("${default.connection.url}")
    private String defaultConnectionUrlValue;
    @Value("${default.connection.instance}")
    private String defaultInstanceName;

    private RequestContext() {
    }


    public static Long getCurrentLocale() {
        return localeThreadLocal.get();
    }

    public static void setCurrentLocale(Long locale) {
        localeThreadLocal.set(locale);
    }

    public static String getCurrentToken() {
        return tokenThreadLocal.get();
    }

    public static void setCurrentToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static void setCurrentInstance(Instance instance) {
        contextInstance.set(instance);
    }

    public static void setTestOwnerString(String s) {
        testOwnerString.set(s);
    }

    public static String getTestOwner() {
        return testOwnerString.get();
    }

    public static Instance getCurrentInstance() {
        return contextInstance.get();
    }

    public static void setOwner(Owner owner) {
        contextOwner.set(owner);
    }

    public static String getDefaultOwnerName() {
        return defaultOwnerName;
    }

    public String getDefaultConnectionUser() {
        return defaultConnectionUser;
    }

    public void setDefaultConnectionUser(String defaultConnectionUser) {
        this.defaultConnectionUser = defaultConnectionUser;
    }

    public static Owner owner() {
        return contextOwner.get();
    }

    public static void setDefaultOwner() {
        contextOwner.set(new Owner(defaultOwnerName));
    }

    public static void setDefaultInstancex() {
        contextInstance.set(new Instance(defaultInstance, defaultUrl));
    }

    public static void setCustomOwner(String ownerName) {
        contextOwner.set(new Owner(ownerName));
    }

    public static void clear() {
        contextOwner.remove();
        localeThreadLocal.remove();
        contextInstance.remove();
        tokenThreadLocal.remove();
    }

    public static void setDefaultOwnerName(String defaultOwnerName) {
        RequestContext.defaultOwnerName = defaultOwnerName;
    }

    public static void setDefaultInstanceName(String defaultInstance) {
        RequestContext.defaultInstance = defaultInstance;
    }

    public static void setDefaultUrl(String defaultUrl) {
        RequestContext.defaultUrl = defaultUrl;
    }

    @Bean
    private void setStaticDefaultOwner() {
        setDefaultOwnerName(defaultConnectionUser);
    }

    @Bean
    private void setstaticDefaultInstance() {
        setDefaultInstanceName(defaultInstanceName);
    }

    @Bean
    private void setStaticDefaultUrl() {
        setDefaultUrl(defaultConnectionUrlValue);
    }
}
