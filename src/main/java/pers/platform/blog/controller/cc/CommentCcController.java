package pers.platform.blog.controller.cc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.platform.blog.model.Comment;
import pers.platform.blog.model.PageBean;
import pers.platform.blog.service.CommentService;
import pers.platform.blog.utils.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员博主Controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/cc/comment")
public class CommentCcController {

    @Resource
    private CommentService commentService;

    @RequestMapping("/list.html")
    public String list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            @RequestParam(value = "state", required = false) String state,
            HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page),
                Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("start", pageBean.getCurrentPage());
        map.put("size", pageBean.getPageSize());
        List<Comment> commentList = commentService.list(map);
        Long total = commentService.getTotal(map);
        JSONObject result = new JSONObject();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class,
                new DateJsonValueProcessor("yyyy-MM-dd"));
        JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/review.html")
    public String delete(
            @RequestParam(value = "ids", required = false) String ids,
            @RequestParam(value = "state", required = false) Integer state,
            HttpServletResponse response) throws Exception {
        String[] idStr = ids.split(",");
        for (int i = 0; i < idStr.length; i++) {
            Comment comment = new Comment();
            comment.setId(idStr[i]);
            comment.setState(state);
            commentService.update(comment);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 评论信息删除
     * 
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete.html")
    public String delete(
            @RequestParam(value = "ids", required = false) String ids,
            HttpServletResponse response) throws Exception {
        String[] idStr = ids.split(",");
        JSONObject result = new JSONObject();
        for (int i = 0; i < idStr.length; i++) {
            commentService.delete(idStr[i]);
        }
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }
}
