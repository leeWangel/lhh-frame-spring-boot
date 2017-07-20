package com.lhh.core.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * Shiro 配置
 */
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroConfiguration {

    @Bean
    public EhCacheManager getEhCacheManager(){
        EhCacheManager ehcacheManager = new EhCacheManager();
        ehcacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return ehcacheManager;
    }
    @Bean
    public Cache<String, ShiroUser> getUserCache() {
        EhCacheManager ehcacheManager = getEhCacheManager();
        Cache<String, ShiroUser> cache = ehcacheManager.getCache("shiroUser");
        return cache;
    }
    
    /**
     * 自定义的认证类，继承自AuthorizingRealm，负责用户的认证和权限的处理
     */
    @Bean
    public Realm userRealm(ShiroProperties shiroProperties) {
        UserRealm userRealm = new UserRealm();
        userRealm.setUserCache(getUserCache());
        return userRealm;
    }

    /**
     * 会话管理
     */
    @Bean
    public SessionManager sessionManager(ShiroProperties shiroProperties) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(shiroProperties.getGlobalSessionTimeout());
        return sessionManager;
    }

    /**
     * Shiro默认会使用Servlet容器的Session,可通过sessionMode属性来指定使用Shiro原生Session
     * 设置自定义的单Realm应用,若有多个Realm,需使用'realms'属性代替
     */
    @Bean
    public SecurityManager securityManager(ShiroProperties shiroProperties) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm(shiroProperties));
        securityManager.setSessionManager(sessionManager(shiroProperties));
        securityManager.setCacheManager(getEhCacheManager());
        return securityManager;
    }

    /**
     * Shiro主过滤器 功能十分强大，其强大之处就在于支持任何基于URL路径表达式的、自定义的过滤器的执行
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(ShiroProperties shiroProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager(shiroProperties));

        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl(shiroProperties.getLogoutUrl());
        filters.put("logout", logoutFilter);
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionManager = shiroProperties.getFilterChainDefinitions();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);

        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        return shiroFilterFactoryBean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(ShiroProperties shiroProperties) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager(shiroProperties));
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * AOP方法级权限检查
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
