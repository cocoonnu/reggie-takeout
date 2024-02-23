package com.cocoon.reggieTakeout.constant;

import java.util.Random;

public class RandomUtil {
    /** 随机生成验证码，默认为4位 **/
    public static String buildCheckCode(Integer digit){
        String str = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int digitValue = digit == null ? 4 : digit;
        for (int i = 0; i < digitValue; i++) {
            char ch = str.charAt(random.nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
