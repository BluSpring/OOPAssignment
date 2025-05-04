package util;

import java.awt.*;

public class ColorUtils {
    public static final Color NONE = new Color(0f, 0f, 0f, 0f);

    /**
     * Converts a hex value to an AWT Color.
     * @param hexRGB The hex value (formatted in RGB) to convert
     * @return The AWT Color with the specified values
     */
    public static Color fromHex(int hexRGB) {
        var r = (hexRGB >> 16) & 255;
        var g = (hexRGB >> 8) & 255;
        var b = hexRGB & 255;

        return fromRGB(r, g, b);
    }

    /**
     * Converts RGB color data to an AWT Color.
     * @param r The red value (0-255)
     * @param g The green value (0-255)
     * @param b The blue value (0-255)
     * @return The AWT Color with the specified values
     */
    public static Color fromRGB(int r, int g, int b) {
        var hsb = Color.RGBtoHSB(r, g, b, null);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
