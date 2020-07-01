package com.github.jiangxch.mybatis.common.util;

/**
 * 字符串工具类
 * @author: jiangxch
 * @date: 2020/5/12 下午10:36
 */
public class StringUtil {

    /**
     * 大驼峰命名法转下划线
     * @param upperCamelCase 大驼峰命名,eg:TestProject
     * @return 下划线命名,eg:test_project
     */
    public static String upperCamelCase2UnderScoreCase(String upperCamelCase) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < upperCamelCase.toCharArray().length; i++) {
            char ch = upperCamelCase.toCharArray()[i];
            // lower case([97,122]) upper case([65,90])
            if (ch >= 65 && ch <= 90) {
                if (i != 0) {
                    sb.append("_");
                }
                // upper case convert to lower case
                sb.append((char)(ch + 32));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(upperCamelCase2UnderScoreCase("accessDate"));
        System.out.println(upperCamelCase2UnderScoreCase("Blog"));
        System.out.println(upperCamelCase2UnderScoreCase("StringUtil"));
    }

}
