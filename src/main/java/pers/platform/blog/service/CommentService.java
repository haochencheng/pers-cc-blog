package pers.platform.blog.service;

import java.util.List;
import java.util.Map;

import pers.platform.blog.model.Comment;

/**
 * 评论Service接口
 * 
 * @author Administrator
 *
 */
public interface CommentService {

    /**
     * 查询用户评论信息
     * 
     * @param map
     * @return
     */
    public List<Comment> list(Map<String, Object> map);

    /**
     * 添加评论
     * 
     * @param comment
     * @return
     */
    public Comment add(Comment comment);

    /**
     * 获取总记录数
     * 
     * @param map
     * @return
     */
    public Long getTotal(Map<String, Object> map);

    /**
     * 修改评论
     * 
     * @param comment
     * @return
     */
    public Comment update(Comment comment);

    /**
     * 删除评论
     * 
     * @param id
     * @return
     */
    public Integer delete(String id);

}
