package pers.platform.blog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pers.platform.blog.model.Link;
import pers.platform.blog.repository.LinkRepo;
import pers.platform.blog.service.LinkService;

/**
 * 友情链接Service实现类
 * 
 * @author Administrator
 *
 */
@Service("linkService")
public class LinkServiceimpl implements LinkService {

    public static final String LINK_LIST = "linkList";

    public static final String LINK = "link_";

    @Resource
    private LinkRepo linkRepo;

    @Override
    @Cacheable(value = "link", key = "#root.target.LINK+#root.target.LINK_LIST", unless = "(#result eq null)||(#result.size()==0)")
    public List<Link> list(Map<String, Object> map) {
        return linkRepo.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return linkRepo.getTotal(map);
    }

    @Override
    @CachePut(value = "link", key = "#root.target.LINK+#link.id.toString()", unless = "#result eq null")
    public Link add(Link link) {
        return linkRepo.add(link) == 1 ? link : null;
    }

    @Override
    @CachePut(value = "link", key = "#root.target.LINK+#link.id.toString()", unless = "#result eq null")
    public Link update(Link link) {
        return linkRepo.update(link) == 1 ? link : null;
    }

    @Override
    @CacheEvict(value = "link", key = "#root.target.LINK+#id.toString()", condition = "#result eq 1")
    public Integer delete(String id) {
        return linkRepo.delete(id);
    }

}
