package util;

import java.util.StringJoiner;
import java.util.function.Consumer;

public class Utils {
    public static String join(String delimiter, Object... values) {
        var strings = new StringJoiner(delimiter);

        for (Object value : values) {
            strings.add(value.toString());
        }

        return strings.toString();
    }

    public static <T> T make(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }
}
