package pers.platform.blog.controller.cc;

import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pers.platform.blog.model.Blogger;
import pers.platform.blog.service.BloggerService;
import pers.platform.blog.utils.CryptographyUtil;
import pers.platform.blog.utils.DateUtil;
import pers.platform.blog.utils.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 管理员博主Controller
 * 
 * @author cc
 *
 */
@Controller
@RequestMapping("/cc/blogger")
public class BloggerCcController {

    @Resource
    private BloggerService bloggerService;

    /**
     * 查询博主信息
     * 
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find.html")
    public String find(HttpServletResponse response) throws Exception {
        Blogger blogger = bloggerService.find("1");
        JSONObject jsonObject = JSONObject.fromObject(blogger);
        ResponseUtil.write(response, jsonObject);
        return null;
    }

    /**
     * 修改博主信息
     * 
     * @param imageFile
     * @param blogger
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/save.html")
    public String save(@RequestParam("imageFile") MultipartFile imageFile,
            Blogger blogger, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!imageFile.isEmpty()) {
            String filePath  = request.getServletContext()
                    .getRealPath("/static/userImages/");
            String imageName = DateUtil.getCurrentDateStr() + "."
                    + imageFile.getOriginalFilename().split("\\.")[1];
            imageFile
                    .transferTo(new File(filePath + imageName));
            blogger.setImageName(imageName);
        }
        int resultTotal = bloggerService.update(blogger) == null ? 0 : 1;
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("status", 1);
        } else {
            result.put("status", 0);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/modifyPassword.html")
    public String modifyPassword(String newPassword,
            HttpServletResponse response) throws Exception {
        Blogger blogger = new Blogger();
        blogger.setPassword(CryptographyUtil.md5(newPassword, "cc"));
        int resultTotal = bloggerService.update(blogger) == null ? 0 : 1;
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
     * 退出系统
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/logout.html")
    public String logout() throws Exception {
        SecurityUtils.getSubject().logout();
        return "redirect:/cc/login";
    }

}
