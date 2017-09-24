package pers.platform.blog.service;

import pers.platform.blog.model.Blogger;

/**
 * 博主Service接口
 * 
 * @author Administrator
 *
 */
public interface BloggerService {

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
    public Blogger update(Blogger blogger);
}
