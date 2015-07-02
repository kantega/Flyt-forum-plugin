package no.kantega.utilities;

import java.util.Collection;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-02
 */
public class Objects {

    private Objects() {}

    public static <T> T requireNonNull(T object, String message) {
        if (object == null)
            throw new NullPointerException(message);
        return object;
    }

    public static <T extends Collection> T requireNonEmpty(T object, String message) {
        if (object.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static <T extends Collection> T requireNonNullElements(T object, String format) {
        int index = 0;
        for (Object element : object) {
            if (element == null) {
                throw new NullPointerException(String.format(format, index));
            }
            index++;
        }
        return object;
    }

    public static boolean nonNull(Object object) {
        return object != null;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }
}
