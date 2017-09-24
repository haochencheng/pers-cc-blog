package pers.platform.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pers.platform.blog.model.Blogger;
import pers.platform.blog.repository.BloggerRepo;
import pers.platform.blog.service.BloggerService;

/**
 * 博主实现类
 * 
 * @author Administrator
 *
 */
@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService {

    public static final String BLOGGER = "blogger_";

    @Autowired
    private BloggerRepo bloggerRepo;

    /*
     * 
     * @param username
     * 
     * @return
     */
    @Override
    @Cacheable(value = "blogger", key = "#root.target.BLOGGER+#userName", unless = "#result eq null")
    public Blogger getByUserName(String userName) {
        return bloggerRepo.getByUserName(userName);
    }

    @Override
    @Cacheable(value = "blogger", key = "#root.target.BLOGGER+#id", unless = "#result eq null")
    public Blogger find(String id) {
        return bloggerRepo.find(id);
    }

    /**
     * @CachePut 应用到写数据的方法上，如新增/修改方法，调用方法时会自动把相应的数据放入缓存
     * @param person
     * @return
     */
    @Override
    @CachePut(value = "blogger", key = "#root.target.BLOGGER+#blogger.id", condition = "#result ne null")
    public Blogger update(Blogger blogger) {
        return bloggerRepo.update(blogger) == 1 ? blogger : null;
    }

}
