package ui;

import util.ColorUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;

public class ComponentHelper {
    public static void setForegroundHoverColor(Component component, Color hoverColor) {
        var originalColor = component.getForeground();

        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                component.setForeground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                component.setForeground(originalColor);
            }
        });
    }

    public static void makeHyperlink(JButton button) {
        button.setOpaque(false);

        // Do not set the generic types, as otherwise an error occurs in compilation.
        Map attributes = button.getFont().getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        button.setFont(button.getFont().deriveFont(attributes));

        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(2, 2, 2, 2));
        button.setBackground(ColorUtils.NONE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setForegroundHoverColor(button, ColorUtils.fromHex(0xFEFF00));
    }
}
