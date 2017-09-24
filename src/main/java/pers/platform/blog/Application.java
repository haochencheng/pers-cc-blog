package pers.platform.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 
@Controller
@SpringBootApplication
@MapperScan("pers.platform.blog.repository") // mybatis自动扫描repository
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "redirect:index.html";
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) { // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(Application.class);
    }

}
