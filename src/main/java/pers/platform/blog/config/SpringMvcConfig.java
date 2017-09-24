package pers.platform.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    /*
     * 自定义视图控制器,不用写controller直接返回视图 (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
     * #addViewControllers(org.springframework.web.servlet.config.annotation.
     * ViewControllerRegistry)
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/cc/main.html").setViewName("/cc/main");
        registry.addViewController("/cc/blog.html").setViewName("/cc/blog");
        registry.addViewController("/cc/commentReview.html")
                .setViewName("/cc/commentReview");
        registry.addViewController("/cc/blogManage.html")
                .setViewName("/cc/blogManage");
        registry.addViewController("/cc/blogTypeManage.html")
                .setViewName("/cc/blogTypeManage");
        registry.addViewController("/cc/commentManage.html")
                .setViewName("/cc/commentManage");
        registry.addViewController("/cc/modifyInfo.html")
                .setViewName("/cc/modifyInfo");
        registry.addViewController("/cc/linkManage.html")
                .setViewName("/cc/linkManage");
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }

}
