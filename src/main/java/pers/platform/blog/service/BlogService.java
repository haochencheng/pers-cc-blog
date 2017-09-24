package pers.platform.blog.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pers.platform.blog.model.Blog;

/**
 * 博客Service接口
 * 
 * @author Administrator
 *
 */
public interface BlogService {

    /**
     * 根据日期月份分组查询查询
     * 
     * @return
     */
    public List<Blog> countList();

    /**
     * 分页查询博客
     * 
     * @param map
     * @return
     */
    public List<Blog> list(Map<String, Object> map);

    /**
     * 获取总记录数
     * 
     * @param map
     * @return
     */
    public Integer getTotal(Map<String, Object> map);

    /**
     * 根据id查询博客
     * 
     * @param id
     * @return
     */
    public Blog findById(String id);

    /**
     * 更新博客信息
     * 
     * @param blog
     * @return
     */
    public Blog update(Blog blog);

    /**
     * 获取上一个博客
     * 
     * @param id
     * @return
     */
    public Blog getLastBlog(Date releaseDate);

    /**
     * 获取下一个博客
     * 
     * @param id
     * @return
     */
    public Blog getNextBlog(Date releaseDate);

    /**
     * 添加博客信息
     * 
     * @param blog
     * @return
     */
    public Blog add(Blog blog);

    /**
     * 删除博客信息
     * 
     * @param i
     * @return
     */
    public Integer delete(String id);

    /**
     * 根据blogTypeId查询博客
     * 
     * @param blogTypeId
     * @return
     */
    public Integer getBlogCountByTypeId(String blogTypeId);
}
