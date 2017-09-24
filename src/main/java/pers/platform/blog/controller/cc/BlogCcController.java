package pers.platform.blog.controller.cc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.platform.blog.lucene.BlogIndex;
import pers.platform.blog.model.Blog;
import pers.platform.blog.model.PageBean;
import pers.platform.blog.service.BlogService;
import pers.platform.blog.service.IdService;
import pers.platform.blog.utils.Base64Util;
import pers.platform.blog.utils.DateUtil;
import pers.platform.blog.utils.ResponseUtil;
import pers.platform.blog.utils.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cc/blog")
public class BlogCcController {

    @Resource
    private BlogService blogService;

    @Resource
    private IdService idService;

    @Resource
    private BlogIndex blogIndex;

    /**
     * 上传粘贴的图片
     * 
     * @param request
     * @param image
     * @return
     * @throws Exception
     * @Description:
     */
    @RequestMapping("/uploadPastImage")
    public String uploadPastImage(HttpServletRequest request, String image)
            throws Exception {
        if (StringUtil.isEmpty(image)) {
            return null;
        }
        // 定义图片存储路径 按月份储存 如 20170903
        String fileDirName = DateUtil.getCurrentDateStr().substring(0, 6);
        String imageFilePath = request.getServletContext()
                .getRealPath("/static/userImages/") + fileDirName;
        File filePath = new File(imageFilePath);
        // 不存在则创建
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        String newFileName = DateUtil.getCurrentDateStr() + ".jpg";
        // 将base64转为jpg .replace("webapp", "resources") 放到resources中前段图片无法显示
        Base64Util.convert64StrToImg(image,
                imageFilePath.replace("webapp", "resources") + "/"
                        + newFileName);
        return "/userImages/" + fileDirName + "/" + newFileName;
    }

    /**
     * ckeditor 上传图片
     * 
     * @param request
     * @param file
     * @param CKEditorFuncNum
     * @return
     * @throws Exception
     * @Description:
     */
    @RequestMapping("/ckeditorUpload")
    public String upLoadImage(HttpServletRequest request,
            @RequestParam("upload") MultipartFile file, String CKEditorFuncNum)
            throws Exception {
        // 定义图片存储路径 按月份储存 如 20170903
        String fileDirName = DateUtil.getCurrentDateStr().substring(0, 6);
        String imageFilePath = request.getServletContext()
                .getRealPath("/static/userImages/")
                .replace("webapp", "resources") + fileDirName;
        File filePath = new File(imageFilePath);
        // 不存在则创建
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = DateUtil.getCurrentDateStr() + suffixName;
        FileUtils.copyInputStreamToFile(file.getInputStream(),
                new File(imageFilePath + "/" + newFileName));
        StringBuffer sb = new StringBuffer();
        sb.append("<script type=\"text/javascript\">");
        sb.append("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum
                + ",'" + "/userImages/" + fileDirName + "/" + newFileName
                + "','')");
        sb.append("</script>");
        return sb.toString();
    }

    @RequestMapping("/modifyBlog.html")
    public ModelAndView modifyBlog(String id, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id", id);
        modelAndView.setViewName("/cc/blog");
        return modelAndView;
    }

    @RequestMapping("/save.html")
    public String save(Blog blog, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        String message;
        if (StringUtil.isEmpty(blog.getId())) {
            blog.setId(idService.getId());
            blog.setClickHit(1);
            blog.setReleaseDate(new Date());
            blog.setReplyHit(0);
            resultTotal = blogService.add(blog) != null ? 1 : 0;
            blogIndex.addIndex(blog);
            message = "博客发布成功!";
        } else {
            // 先查询,发生改变的 设置 然后更新.
            Blog oldBlog = blogService.findById(blog.getId());
            oldBlog.setTitle(blog.getTitle());
            oldBlog.setClickHit(oldBlog.getClickHit() + 1);
            oldBlog.setContent(blog.getContent());
            oldBlog.setBlogType(blog.getBlogType());
            oldBlog.setKeyWord(blog.getKeyWord());
            resultTotal = blogService.update(oldBlog) != null ? 1 : 0;
            blogIndex.updateIndex(blog);
            message = "博客修改成功!";
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
            result.put("message", message);
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
     * @param s_bBlog
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list.html")
    public String list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            Blog s_bBlog, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page),
                Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("title", StringUtil.formatLike(s_bBlog.getTitle()));
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<Blog> blogList = blogService.list(map);
        Integer total = blogService.getTotal(map);
        JSONObject result = new JSONObject();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class,
                new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray jsonArray = JSONArray.fromObject(blogList, jsonConfig);
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
        for (int i = 0; i < idStr.length; i++) {
            blogService.delete(idStr[i]);
            blogIndex.deleteindex(idStr[i]);
        }
        JSONObject result = new JSONObject();
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
        Blog blog = blogService.findById(id);
        JSONObject result = JSONObject.fromObject(blog);
        ResponseUtil.write(response, result);
        return null;
    }

}
