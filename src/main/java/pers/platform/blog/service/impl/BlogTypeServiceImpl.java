package pers.platform.blog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import pers.platform.blog.model.BlogType;
import pers.platform.blog.repository.BlogTypeRepo;
import pers.platform.blog.service.BlogTypeService;

/**
 * 博客类型Service实现类
 * 
 * @author Administrator
 *
 */
@Service("blogTypeService")
public class BlogTypeServiceImpl implements BlogTypeService {

    public static final String BlOGTYPE = "blogType_";

    @Resource
    private BlogTypeRepo blogTypeRepo;

    @Override
    public List<BlogType> countList() {
        return blogTypeRepo.countList();
    }

    @Override
    public BlogType findById(String id) {
        return blogTypeRepo.findById(id);
    }

    @Override
    public List<BlogType> list(Map<String, Object> map) {
        return blogTypeRepo.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return blogTypeRepo.getTotal(map);
    }

    @Override
    @CachePut(value = "blogType", key = "#root.target.BlOGTYPE+#blogType.id", unless = "#result eq null")
    public BlogType add(BlogType blogType) {
        return blogTypeRepo.add(blogType) == 1 ? blogType : null;
    }

    @Override
    @CachePut(value = "blogType", key = "#root.target.BlOGTYPE+#blogType.id", unless = "#result eq null")
    public BlogType update(BlogType blogType) {
        return blogTypeRepo.update(blogType) == 1 ? blogType : null;
    }

    @Override
    @CacheEvict(value = "blogType", key = "#root.target.BlOGTYPE+#id", condition = "#result eq 1")
    public Integer delete(String id) {
        return blogTypeRepo.delete(id);
    }

}
