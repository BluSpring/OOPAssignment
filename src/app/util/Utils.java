package app.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
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

    public static List<String> allToStrings(Object... values) {
        var list = new ArrayList<String>();

        for (Object value : values) {
            list.add(value.toString());
        }

        return list;
    }

    public static <T> T make(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

    public static BufferedImage getCircularImage(BufferedImage image) {
        var output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g2 = output.createGraphics();

        // This creates an alpha mask for the image to use when being drawn.
        // Although g2.clip exists, we prefer this as we will have antialiasing enabled
        // (aka smooth edges vs jagged edges)
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(0, 0, image.getWidth(), image.getHeight()));

        // Then, we draw the image on top, being masked by the alpha mask we had just made.
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }

    public static Image createEmptyImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
