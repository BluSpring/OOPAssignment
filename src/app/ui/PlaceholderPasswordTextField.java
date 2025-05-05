package app.ui;

import javax.swing.*;
import java.awt.*;

public class PlaceholderPasswordTextField extends JPasswordField {
    private String placeholder;

    public PlaceholderPasswordTextField(String placeholder) {
        super("");
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(2, 2, this.getWidth() - 4, this.getHeight() - 4);

        super.paintComponent(g);

        if (placeholder == null || placeholder.isBlank() || !this.getText().isBlank())
            return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.getDisabledTextColor());
        g2d.drawString(this.getPlaceholder(), this.getInsets().left, g2d.getFontMetrics().getMaxAscent() + this.getInsets().top);
    }
}
