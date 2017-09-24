package pers.platform.blog.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.platform.blog.model.Link;

import java.util.List;
import java.util.Map;

/**
 * 友情链接Dap接口
 * 
 * @author Administrator
 *
 */
@Mapper
public interface LinkRepo {

    /**
     * 查询友情链接信息
     * 
     * @param map
     * @return
     */
    public List<Link> list(Map<String, Object> map);

    /**
     * 获取总记录数
     * 
     * @param map
     * @return
     */
    public Long getTotal(Map<String, Object> map);

    /**
     * 添加友情链接
     * 
     * @param link
     * @return
     */
    public Integer add(Link link);

    /**
     * 更新友情链接
     * 
     * @param link
     * @return
     */
    public Integer update(Link link);

    /**
     * 删除友情链接
     * 
     * @param id
     * @return
     */
    public Integer delete(String id);

}
