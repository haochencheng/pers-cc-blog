package pers.platform.blog.controller.cc;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * json-lib 日期处理类
 * 
 * @author Administrator
 *
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

    private String format;

    public DateJsonValueProcessor(String format) {
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object arg0, JsonConfig arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object processObjectValue(String key, Object value,
            JsonConfig jsonConfig) {
        if (value == null) {
            return "";
        }
        if (value instanceof java.sql.Timestamp) {
            return new SimpleDateFormat(format)
                    .format((java.sql.Timestamp) value);
        }
        if (value instanceof java.util.Date) {
            return new SimpleDateFormat(format).format((java.util.Date) value);
        }
        return value.toString();
    }

}
