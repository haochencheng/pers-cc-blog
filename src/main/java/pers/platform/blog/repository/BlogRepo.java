package pers.platform.blog.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.platform.blog.model.Blog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 博客Dao接口
 * 
 * @author Administrator
 *
 */
@Mapper
public interface BlogRepo {

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
    public Integer update(Blog blog);

    /**
     * 获取上一个博客
     * 
     * @param id
     * @return
     */
    public Blog getLastBlog(Date realseDate);

    /**
     * 获取下一个博客
     * 
     * @param id
     * @return
     */
    public Blog getNextBlog(Date realseDate);

    /**
     * 添加博客信息
     * 
     * @param blog
     * @return
     */
    public Integer add(Blog blog);

    /**
     * 删除博客信息
     * 
     * @param id
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
