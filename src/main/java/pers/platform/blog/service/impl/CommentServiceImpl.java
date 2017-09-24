package pers.platform.blog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import pers.platform.blog.model.Comment;
import pers.platform.blog.repository.CommentRepo;
import pers.platform.blog.service.CommentService;

/**
 * 评论Service实现类
 * 
 * @author Administrator
 *
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    public static final String COMMENT = "commont_";

    @Resource
    private CommentRepo commentRepo;

    @Override
    @CachePut(value = "comment", key = "#root.target.COMMENT+#map.get('blogId')", unless = "(#result eq null)")
    public List<Comment> list(Map<String, Object> map) {
        return commentRepo.list(map);
    }

    @Override
    @CachePut(value = "comment", key = "#root.target.COMMENT+#comment.id.toString()", unless = "#result eq null")
    public Comment add(Comment comment) {
        return commentRepo.add(comment) == 1 ? comment : null;
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return commentRepo.getTotal(map);
    }

    @Override
    @CachePut(value = "comment", key = "#root.target.COMMENT+#comment.id", unless = "#result eq null")
    public Comment update(Comment comment) {
        return commentRepo.update(comment) == 1 ? comment : null;
    }

    @Override
    @CacheEvict(value = "comment", key = "#root.target.COMMENT+#id", condition = "#result eq 1")
    public Integer delete(String id) {
        return commentRepo.delete(id);
    }

}
