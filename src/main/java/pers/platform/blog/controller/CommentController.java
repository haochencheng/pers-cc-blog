package pers.platform.blog.controller;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.platform.blog.model.Blog;
import pers.platform.blog.model.Comment;
import pers.platform.blog.service.BlogService;
import pers.platform.blog.service.CommentService;
import pers.platform.blog.service.IdService;
import pers.platform.blog.utils.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 评论Controller层
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private BlogService blogService;

    @Resource
    private IdService idservice;

    /**
     * 添加或者修改评论
     * 
     * @param comment
     * @param imageCode
     * @param request
     * @param response
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/save.html")
    public String save(Comment comment,
            @RequestParam("imageCode") String imageCode,
            HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws Exception {
        String sRand = (String) session.getAttribute("sRand");
        JSONObject result = new JSONObject();
        int resultTotal = 0;
        if (!imageCode.equals(sRand)) {
            result.put("success", false);
            result.put("errorInfo", "验证码填写错误");
        } else {
            String userIp = request.getRemoteAddr(); // 获取用户Ip
            comment.setUserIp(userIp);
            if (comment.getId() == null) {
                comment.setId(idservice.getId());
                comment.setCommentDate(new Date());
                resultTotal = commentService.add(comment) != null ? 1 : 0;
                Blog blog = blogService.findById(comment.getBlog().getId());
                blog.setReplyHit(blog.getReplyHit() + 1);
                blogService.update(blog);
            } else {

            }
        }
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

}
