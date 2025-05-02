import admin.Admin;
import auth.AccountType;
import customer.Customer;
import seller.Seller;
import util.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {
    private static final JFrame frame = new JFrame() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var g2d = (Graphics2D) g;
            // Makes the rendering use "quality" rendering
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            var width = this.getWidth();
            var height = this.getHeight();
            var gradient = new GradientPaint(0f, 0f, ColorUtils.fromHex(0x2c70f0), 0f, height, ColorUtils.fromHex(0x25358e));

            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        }

        @Override
        protected void processKeyEvent(KeyEvent e) {
            super.processKeyEvent(e);

            // Forced reload
            if (e.getKeyCode() == KeyEvent.VK_F5) {
                createLogin();
            }
        }
    };

    public static JFrame getFrame() {
        return frame;
    }

    public static void reset() {
        frame.removeAll();
    }

    public static void refresh() {
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public static void createLogin() {
        reset();

        var mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        {
            var panel = new JPanel(new FlowLayout());

            panel.add(new JLabel("Login as: "));
            panel.add(new JComboBox<>(AccountType.values()));

            mainPanel.add(panel);
        }

        frame.add(mainPanel);

        refresh();
    }

    public static void main(String[] args) {
        frame.setSize(843, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Admin.init();
        Customer.init();
        Seller.init();

        createLogin();
    }
}
