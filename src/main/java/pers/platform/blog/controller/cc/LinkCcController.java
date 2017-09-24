package pers.platform.blog.controller.cc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.platform.blog.model.Link;
import pers.platform.blog.model.PageBean;
import pers.platform.blog.service.IdService;
import pers.platform.blog.service.LinkService;
import pers.platform.blog.utils.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cc/link")
public class LinkCcController {

    @Resource
    private LinkService linkService;

    @Resource
    private IdService idService;

    @RequestMapping("/save.html")
    public String save(Link link, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        if (link.getId() == null) {
            link.setId(idService.getId());
            resultTotal = linkService.add(link) == null ? 0 : 1;
        } else {
            resultTotal = linkService.update(link) == null ? 0 : 1;
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
     * @param s_bLink
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list.html")
    public String list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page),
                Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getCurrentPage());
        map.put("size", pageBean.getPageSize());
        List<Link> linkList = linkService.list(map);
        Long total = linkService.getTotal(map);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(linkList);
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
            linkService.delete(idStr[i]);
        }
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

}
