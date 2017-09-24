package pers.platform.blog.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.platform.blog.model.Comment;

import java.util.List;
import java.util.Map;

/**
 * 评论Dao接口
 * 
 * @author Administrator
 *
 */
@Mapper
public interface CommentRepo {

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
    public int add(Comment comment);

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
    public Integer update(Comment comment);

    /**
     * 删除评论
     * 
     * @param id
     * @return
     */
    public Integer delete(String id);

}
