package pers.platform.blog.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pers.platform.blog.model.Blog;
import pers.platform.blog.service.BlogService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class BlogApi {

    @Resource
    private BlogService blogService;

    @GetMapping(value = "/api/blog/filter/{id}")
    public Integer filter(@PathVariable @NotNull String id) {
        Blog blog = blogService.findById(id);
        String content = blog.getContent();
        blog.setContent(filterStyle("style", content));
        return blogService.update(blog) == null ? 0 : 1;
    }

    @GetMapping(value = "/api/blog/filter/all")
    public Integer filter() {
        List<Blog> blogList = blogService.list(null);
        blogList.forEach(blog -> {
            String content = blog.getContent();
            blog.setContent(filterStyle("style", content));
            blogService.update(blog);
        });
        return 1;
    }

    /**
     * 使用正则表达式过滤标签样式:style,class.比如
     * <p style="width:100px">
     * 或
     * <p class="myclass">
     * </p>
     * 
     * @param element
     *            替换的元素标签
     * @param content
     *            替换的文本内容
     */
    private String filterStyle(String element, String content) {
        Pattern pattern = Pattern
                .compile(element + "\\s?=\\s?(['\"][^'\"]*?['\"]|[^'\"]\\S*)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            content = matcher.replaceAll("");
        }
        return content;
    }

}
