package pers.platform.blog.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import pers.platform.blog.realm.MyRealm;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * Shiro的Web过滤器Factory 命名:shiroFilter<br />
     * * * @param securityManager * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            SecurityManager securityManager) {
        logger.info("注入Shiro的Web过滤器-->shiroFilter");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 要求登录时的链接(可根据项目的URL进行替换),非必须的属性,默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/cc/login.html");
        // 登录成功后要跳转的连接,逻辑也可以自定义，例如返回上次请求的页面
        shiroFilterFactoryBean.setSuccessUrl("/cc/main.jsp");
        // 用户访问未对其授权的资源时,所显示的连接
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        /*
         * 定义shiro过滤器,例如实现自定义的FormAuthenticationFilter，
         * 需要继承FormAuthenticationFilter **本例中暂不自定义实现，在下一节实现验证码的例子中体现
         */
        /*
         * 定义shiro过滤链 Map结构 *
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.
         * getContextPath()的值来的 *
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种 *
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.
         * authc.FormAuthenticationFilter
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/foreground/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/static/userImages", "anon");
        filterChainDefinitionMap.put("/api/**", "authc");
        filterChainDefinitionMap.put("/cc/**", "authc");
        filterChainDefinitionMap.put("/druid/**", "authc");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyRealm myShiroRealm() {
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }

    /**
     * 不指定名字的话，自动创建一个方法名第一个字母小写的bean * @Bean(name = "securityManager") * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = SecurityManager.class)
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

}
