package pers.platform.blog.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(value = { "pers.platform.blog" }) // 自动扫描包
@MapperScan("pers.platform.blog.repository") // mybatis自动扫描repository
public class SpringConfig extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(SpringConfig.class);
    }

}
