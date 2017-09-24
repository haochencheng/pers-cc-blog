package pers.platform.blog.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 判断是否是空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return "".equals(str) || str == null;

    }

    /**
     * 判断是否不是空
     * 
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !"".equals(str) && str != null;
    }

    /**
     * 判断str在strArr[]是否存在
     * 
     * @param str
     * @return
     */
    public static boolean existstrArr(String str, String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(str))
                return true;
        }
        return false;
    }

    /**
     * 初始化模糊查询
     * 
     * @param str
     * @return
     */
    public static String formatLike(String str) {
        if (isNotEmpty(str)) {
            return "%" + str + "%";
        } else {
            return null;
        }
    }

    /**
     * 过滤集合中的空格
     * 
     * @param list
     * @return
     */
    public static List<String> filterWhite(List<String> list) {
        List<String> resultList = new ArrayList<>();
        for (String l : list) {
            if (isNotEmpty(l)) {
                resultList.add(l);
            }
        }
        return resultList;
    }

    public static boolean filterSpecialChar(String str) {
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static void main(String[] args) {
        String str = "adsds";
        System.out.println(filterSpecialChar(str));
    }

}
