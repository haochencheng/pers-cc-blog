package pers.platform.blog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pers.platform.blog.model.Blog;
import pers.platform.blog.repository.BlogRepo;
import pers.platform.blog.service.BlogService;

@Service("blogService")
public class BlogServiceImpl implements BlogService {

    /*
     * Spring提供了如下注解来声明缓存规则：
     * 
     * @Cacheable triggers cache population
     * 
     * @CacheEvict triggers cache eviction
     * 
     * @CachePut updates the cache without interfering with the method execution
     * 
     * @Caching regroups multiple cache operations to be applied on a method
     * 
     * @CacheConfig shares some common cache-related settings at class-level 注 解
     * 描 述
     * 
     * @Cacheable
     * 表明Spring在调用方法之前，首先应该在缓存中查找方法的返回值。如果这个值能够找到，就会返回缓存的值。否则的话，这个方法就会被调用，
     * 返回值会放到缓存之中
     * 
     * @CachePut 表明Spring应该将方法的返回值放到缓存中。在方法的调用前并不会检查缓存，方法始终都会被调用
     * 
     * @CacheEvict 表明Spring应该在缓存中清除一个或多个条目
     * 
     * @Caching 这是一个分组的注解，能够同时应用多个其他的缓存注解
     * 
     * @CacheConfig 可以在类层级配置一些共用的缓存配置
     * 
     * @Cacheable和@CachePut有一些共有的属性：
     * 
     * 属 性 类 型 描 述 value String[] 要使用的缓存名称 condition String
     * SpEL表达式，如果得到的值是false的话，不会将缓存应用到方法调用上 key String SpEL表达式，用来计算自定义的缓存key
     * unless String SpEL表达式，如果得到的值是true的话，返回值不会放到缓存之中
     */

    /**
     * It seems that Spring doesn't allow you to provide a static text for the
     * cache key in the SPEL, and it doesn't include as default the name of the
     * method on the key, so, you could be in a situation when two methods using
     * the same cacheName and without a key would potentially cache different
     * results with the same key. 缓存key使用静态变量blog_ + id 方式 保证 curd 为同一key
     */
    public static final String BLOG = "blog_";

    @Resource
    private BlogRepo blogRepo;

    /*
     * (non-Javadoc)
     * 
     * @see com.cc.service.BlogService#countList()
     */
    @Override
    public List<Blog> countList() {
        return blogRepo.countList();
    }

    @Override
    public List<Blog> list(Map<String, Object> map) {
        return blogRepo.list(map);
    }

    @Override
    public Integer getTotal(Map<String, Object> map) {
        return blogRepo.getTotal(map);
    }

    /*
     * 注释： 这里的缓存Key为简单的字符串组合，也可以根据具体需要实现自定义的key生成器，然后在注解中使用keyGenerator来引用
     * 
     * @Cacheable 应用到读取数据的方法上，先从缓存中读取，如果没有再从DB获取数据，然后把数据添加到缓存中
     * unless*表示条件表达式成立的话不放入缓存
     */
    @Override // 将Integer转化为String
    @Cacheable(value = "blog", key = "#root.target.BLOG+#id", unless = "#result eq null")
    public Blog findById(String id) {
        return blogRepo.findById(id);
    }

    @Override
    @CachePut(value = "blog", key = "#root.target.BLOG+#blog.id", unless = "#result eq null")
    public Blog update(Blog blog) {
        return blogRepo.update(blog) == 1 ? blog : null;
    }

    @Override
    @Cacheable(value = "blog", key = "#root.target.BLOG+#realseDate.toString()+'_pre'", unless = "#result eq null")
    public Blog getLastBlog(Date realseDate) {
        return blogRepo.getLastBlog(realseDate);
    }

    @Override
    @Cacheable(value = "blog", key = "#root.target.BLOG+#realseDate.toString()+'_next'", unless = "#result eq null")
    public Blog getNextBlog(Date realseDate) {
        return blogRepo.getNextBlog(realseDate);
    }

    @Override
    @CachePut(value = "blog", key = "#root.target.BLOG+#blog.id.toString()", unless = "#result eq null")
    public Blog add(Blog blog) {
        return blogRepo.add(blog) == 1 ? blog : null;
    }

    @Override
    @CacheEvict(value = "blog", key = "#root.target.BLOG+#id", condition = "#result eq 1")
    public Integer delete(String id) {
        return blogRepo.delete(id);
    }

    @Override
    public Integer getBlogCountByTypeId(String blogTypeId) {
        return blogRepo.getBlogCountByTypeId(blogTypeId);
    }

}
