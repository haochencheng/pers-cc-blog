package pers.platform.blog.realm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import pers.platform.blog.model.Blogger;
import pers.platform.blog.service.BloggerService;

/**
 * 自定义Realm
 * 
 * @author Administrator
 *
 */
@Component
public class MyRealm extends AuthorizingRealm {

    Logger logger = LoggerFactory.getLogger(MyRealm.class);

    @Resource
    @Lazy
    BloggerService bloggerService;

    /*
     * (non-Javadoc) 为当前的登录的用户授予角色和权限
     * 
     * @see
     * org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache
     * .shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        logger.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Blogger bloggerInfo = (Blogger) principals.getPrimaryPrincipal();
        logger.info(bloggerInfo.toString());
        return simpleAuthorizationInfo;
    }

    /*
     * (non-Javadoc) 验证当前登录的用户
     * 
     * @see
     * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.
     * apache.shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        logger.info("MyRealm.doGetAuthenticationInfo()");
        String userName = (String) token.getPrincipal();
        Blogger blogger = bloggerService.getByUserName(userName);
        if (blogger != null) {
            logger.info("---------->>bloggerInfo" + blogger.toString());
            SecurityUtils.getSubject().getSession().setAttribute("currentUser",
                    blogger);
            return new SimpleAuthenticationInfo(blogger.getUserName(),
                    blogger.getPassword(), "xxx");
        }
        return null;
    }

}
