package pers.platform.blog.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pers.platform.blog.config.SpringConfig;
import pers.platform.blog.model.Blog;
import pers.platform.blog.model.BlogType;
import pers.platform.blog.model.Blogger;
import pers.platform.blog.model.Link;
import pers.platform.blog.service.BlogService;
import pers.platform.blog.service.BlogTypeService;
import pers.platform.blog.service.BloggerService;
import pers.platform.blog.service.LinkService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.List;

@Component
@AutoConfigureAfter(SpringConfig.class)
public class InitComponent implements ApplicationContextAware {

    private final Logger logger = LogManager.getLogger(InitComponent.class);

    private static ApplicationContext applicationContext;

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext application)
                    throws ServletException {
                BloggerService bloggerService = (BloggerService) applicationContext
                        .getBean("bloggerService");
                Blogger blogger = bloggerService.find("1");
                blogger.setPassword(null);
                application.setAttribute("blogger", blogger);
                LinkService linkService = (LinkService) applicationContext
                        .getBean("linkService");
                List<Link> linkList = linkService.list(null); // 查询所有友情链接
                application.setAttribute("linkList", linkList);
                // 查询博客类别及博客数量
                BlogTypeService blogTypeService = (BlogTypeService) applicationContext
                        .getBean("blogTypeService");
                List<BlogType> blogTypeCountList = blogTypeService.countList();
                application.setAttribute("blogTypeCountList",
                        blogTypeCountList);
                // 根据日期分组查询博客
                BlogService blogService = (BlogService) applicationContext
                        .getBean("blogService");
                List<Blog> blogCountList = blogService.countList();
                application.setAttribute("blogCountList", blogCountList);
                logger.info("初始化公共信息完成!");
            }
        };

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        InitComponent.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

}
