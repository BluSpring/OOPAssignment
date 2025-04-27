package util;

import java.util.StringJoiner;

public class StringUtils {
    public static String join(String delimiter, Object... values) {
        var strings = new StringJoiner(delimiter);

        for (Object value : values) {
            strings.add(value.toString());
        }

        return strings.toString();
    }
}
