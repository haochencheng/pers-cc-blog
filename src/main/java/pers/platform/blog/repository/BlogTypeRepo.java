package pers.platform.blog.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.platform.blog.model.BlogType;

import java.util.List;
import java.util.Map;

/**
 * 博客类型实体
 * 
 * @author Administrator
 *
 */
@Mapper
public interface BlogTypeRepo {

    /**
     * 查询所有博客类型以及对应的博客数量
     * 
     * @return
     */
    public List<BlogType> countList();

    /**
     * 通过id查找博客类型实体
     * 
     * @return
     */
    public BlogType findById(String id);

    /**
     * 分页查询博客
     * 
     * @param map
     * @return
     */
    public List<BlogType> list(Map<String, Object> map);

    /**
     * 获取总记录数
     * 
     * @param map
     * @return
     */
    public Long getTotal(Map<String, Object> map);

    /**
     * 添加博客类别
     * 
     * @param blogType
     * @return
     */
    public Integer add(BlogType blogType);

    /**
     * 更新博客类别
     * 
     * @param blogType
     * @return
     */
    public Integer update(BlogType blogType);

    /**
     * 删除博客
     * 
     * @param id
     * @return
     */
    public Integer delete(String id);

}
