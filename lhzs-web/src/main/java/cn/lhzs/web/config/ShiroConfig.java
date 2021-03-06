package cn.lhzs.web.config;

import cn.lhzs.web.shiro.ShiroRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ZHX
 * @descption shiro配置
 */
@Configuration
public class ShiroConfig {

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

}
