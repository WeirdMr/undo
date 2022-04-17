package top.cafebabe.undo.file.util;

import java.text.SimpleDateFormat;

/**
 * @author cafababe
 */
public class NowUtil {
    private static SimpleDateFormat simpleDateFormat;

    static {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public static String nowTime() {
        return simpleDateFormat.format(System.currentTimeMillis());
    }
}
