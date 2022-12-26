package top.shjibi.plugineer.util;

import java.util.List;
import java.util.regex.Pattern;

/** 与字符串相关的工具类 */
public final class StringUtil {

    private StringUtil() {}

    private static final Pattern colorPattern = Pattern.compile("&([\\da-fk-orx])");
    private static final Pattern stripPattern = Pattern.compile("[&§]([\\da-fk-orx])");

    /** 给消息染色 */
    public static String color(String s) {
        return colorPattern.matcher(s).replaceAll("§$1");
    }

    /** 去掉消息的颜色 */
    public static String strip(String s) {
        return stripPattern.matcher(s).replaceAll("");
    }

    /**
     * 从指定字符串移除s
     * @param s 给定的字符串
     * @param remove 需要移除的字符串
     * @return 移除remove后的字符串s
     */
    public static String remove(String s, String... remove) {
        for (String r : remove) {
            s = s.replace(r, "");
        }
        return s;
    }

    /** 在elements之间插入s */
    public static String join(String s, String... elements) {
        return join(s, 0, elements);
    }

    /** 在elements中，从start开始，插入s */
    public static String join(String s, int start, String... elements) {
        if (start >= elements.length) start = 0;
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < elements.length; i++) {
            builder.append(elements[i]).append(s);
        }

        return builder.substring(0, builder.length() - 1);
    }

    /** 在elements之间插入s */
    public static String join(String s, List<String> elements) {
        return join(s, elements.toArray(new String[0]));
    }
}
