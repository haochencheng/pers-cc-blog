package pers.platform.blog.controller.cc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.platform.blog.model.BlogType;
import pers.platform.blog.model.PageBean;
import pers.platform.blog.service.BlogService;
import pers.platform.blog.service.BlogTypeService;
import pers.platform.blog.service.IdService;
import pers.platform.blog.utils.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/cc/blogType")
public class BlogTypeCcController {

    @Resource
    private BlogTypeService blogTypeService;

    @Resource
    private IdService idService;

    @Resource
    private BlogService blogService;

    @RequestMapping("/save.html")
    public String save(BlogType blogType, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        if (blogType.getId() == null) {
            blogType.setId(idService.getId());
            resultTotal = blogTypeService.add(blogType) == null ? 0 : 1;
        } else {
            resultTotal = blogTypeService.update(blogType) == null ? 0 : 1;
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 分页查询博客信息
     * 
     * @param page
     * @param rows
     * @param s_bBlogType
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list.html")
    public String list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            BlogType s_bBlogType, HttpServletResponse response)
            throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page),
                Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getCurrentPage());
        map.put("size", pageBean.getPageSize());
        List<BlogType> blogTypeList = blogTypeService.list(map);
        Long total = blogTypeService.getTotal(map);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(blogTypeList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 博客信息删除
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
            int sum = blogService.getBlogCountByTypeId(idStr[i]);
            if (sum > 0) {
                result.put("exist", "博客类别下有&nbsp;<font color='red' >" + sum
                        + "</font>&nbsp;篇博客,不能删除！");
            } else {
                blogTypeService.delete(idStr[i]);
            }
        }
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 通过id查找博客
     * 
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/findById.html")
    public String findById(@RequestParam(value = "id") String id,
            HttpServletResponse response) throws Exception {
        BlogType blogType = blogTypeService.findById(id);
        JSONObject result = JSONObject.fromObject(blogType);
        ResponseUtil.write(response, result);
        return null;
    }

}
