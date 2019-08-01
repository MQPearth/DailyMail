package com.github.mqpearth.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FormatUtils {


    private static final Pattern mailPattern = Pattern.compile("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}");//邮箱格式

    private static final Pattern ipPattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");//ip格式

    /**
     * 邮箱格式校验
     *
     * @param mail
     * true 匹配
     * false 不匹配
     * @return
     */
    public boolean checkMail(String mail) {
        Matcher m = mailPattern.matcher(mail);
        return m.matches();
    }

    /**
     * 参数校验
     * 检查字符串是否 为 null 为 ""
     * 为null 或 ""都返回 false
     *
     * @param strs 动态参数
     */
    public boolean checkStringNull(String... strs) {
        for (String str : strs) {
            if (str == null || "".equals(str))
                return false;
        }
        return true;
    }
}
