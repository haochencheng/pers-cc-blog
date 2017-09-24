package pers.platform.blog.repository;

import org.apache.ibatis.annotations.Mapper;
import pers.platform.blog.model.Blogger;

/**
 * 博主Dao接口
 * 
 * @author Administrator
 *
 */
@Mapper
public interface BloggerRepo {

    /**
     * 通过用户名查询用户
     * 
     * @param userName
     * @return
     */
    public Blogger getByUserName(String userName);

    /**
     * 查询博主信息
     * 
     * @param id
     * @return
     */
    public Blogger find(String id);

    /**
     * 更新博主信息
     * 
     * @param blogger
     * @return
     */
    public Integer update(Blogger blogger);

}
