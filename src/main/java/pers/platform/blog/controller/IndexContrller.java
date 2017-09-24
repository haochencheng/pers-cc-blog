package pers.platform.blog.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pers.platform.blog.model.Blog;
import pers.platform.blog.model.PageBean;
import pers.platform.blog.service.*;
import pers.platform.blog.utils.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页Contrller
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexContrller {

    @Resource
    private BlogService blogService;

    @Resource
    private BloggerService bloggerService;

    @Resource
    private BlogTypeService blogTypeService;
    @Resource
    private LinkService linkService;

    @Resource
    private CommentService commentService;

    /**
     * 请求主页
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "typeId", required = false) String typeId,
            @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr,
            HttpServletRequest request) throws Exception {
        ModelAndView mAndView = new ModelAndView();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        PageBean pageBean = new PageBean(Integer.parseInt(page), 10);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("releaseDateStr", releaseDateStr);
        map.put("typeId", typeId);
        List<Blog> blogList = blogService.list(map);
        for (Blog blog : blogList) {
            List<String> imageList = blog.getImageList();
            String blogIngo = blog.getContent();
            Document doc = Jsoup.parse(blogIngo);
            Elements jpgs = doc.select("img[src$=.jpg]");
            for (int i = 0; i < jpgs.size(); i++) {
                Element jpg = jpgs.get(i);
                if (jpg.toString().startsWith("/static")
                        || jpg.toString().contains("/static")) {
                    imageList.add(jpg.toString().replace("/static", ""));
                } else {
                    imageList.add(jpg.toString());
                }
                if (i == 2) {
                    break;
                }
            }
        }
        mAndView.addObject("blogList", blogList);
        StringBuffer param = new StringBuffer();
        if (StringUtil.isNotEmpty(typeId)) {
            param.append("typeId=" + typeId + "&");
        }
        if (StringUtil.isNotEmpty(releaseDateStr)) {
            param.append("releaseDateStr=" + releaseDateStr + "&");
        }
        pageBean.setTotalPages(blogService.getTotal(map));
        if (StringUtil.isNotEmpty(typeId)) {
            mAndView.addObject("typeId", typeId);
        }
        mAndView.addObject("page", pageBean);
        mAndView.addObject("pageTitle", "叶晨个人博客系统");
        mAndView.addObject("mainPage", "foreground/blog/list");
        mAndView.setViewName("mainTemp");
        return mAndView;
    }

    /**
     * 源码下载
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/download.html")
    public ModelAndView download() throws Exception {
        ModelAndView mAndView = new ModelAndView();
        mAndView.addObject("pageTitle", "本站源码下载_cc个人博客系统");
        mAndView.addObject("mainPage", "/foreground/system/download");
        mAndView.setViewName("mainTemp");
        return mAndView;
    }
}
