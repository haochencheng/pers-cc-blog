package pers.platform.blog.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pers.platform.blog.model.Blogger;
import pers.platform.blog.service.BloggerService;
import pers.platform.blog.utils.CryptographyUtil;
import pers.platform.blog.utils.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 博主Controller层
 * 
 * @author Administrator
 *
 */
@Controller
public class BloggerController {

    @Resource
    private BloggerService bloggerService;

    @RequestMapping("/cc/login.html")
    public String login(Blogger blogger, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Blogger blogger2 = (Blogger) request.getSession()
                    .getAttribute("currentUser");
            System.out.println(blogger2);
            if (request.getSession().getAttribute("currentUser") != null) {
                return "redirect:/cc/main.html";
            }
        }
        String userName = blogger.getUserName();
        String password = blogger.getPassword();
        if (blogger == null || StringUtil.isEmpty(userName)
                || StringUtil.isEmpty(password)) {
            return "/cc/login";
        }
        // 过滤特殊字符
        if (StringUtil.filterSpecialChar(password)) {
            request.setAttribute("blogger", blogger);
            request.setAttribute("errorInfo", "密码不能含有特殊字符!");
            return "/cc/login";
        }
        // 默认记住我
        UsernamePasswordToken token = new UsernamePasswordToken(userName,
                CryptographyUtil.md5(password, "cc"), false);
        try {
            subject.login(token); // 登录验证
            return "redirect:/cc/main.html";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("blogger", blogger);
            request.setAttribute("errorInfo", "用户名或密码错误");
            return "/cc/login";
        }
    }

    /**
     * 关于博主
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/blogger/aboutMe.html")
    public ModelAndView aboutMe() throws Exception {
        ModelAndView mAndView = new ModelAndView();
        mAndView.addObject("pageTitle", "关于博主_cc个人博客系统");
        mAndView.addObject("mainPage", "foreground/blogger/info");
        mAndView.setViewName("mainTemp");
        return mAndView;
    }

}
