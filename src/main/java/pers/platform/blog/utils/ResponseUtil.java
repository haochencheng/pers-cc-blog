package pers.platform.blog.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseUtil {

    public static void write(HttpServletResponse response, Object o)
            throws Exception {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(o.toString());
        out.flush();
        out.close();
    }
}
