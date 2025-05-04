import admin.Admin;
import auth.AccountType;
import customer.Customer;
import seller.Seller;
import util.ColorUtils;
import util.SwingComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {
    private static final JFrame window = new JFrame() {
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
        return window;
    }

    public static void reset() {
        window.getContentPane().removeAll();
    }

    public static void refresh() {
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public static void createLogin() {
        reset();

        var mainPanel = new JPanel(new GridLayout(5, 1));

        {
            var panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            panel.add(new JLabel("Login as: "));
            panel.add(new SwingComponentBuilder<>(new JComboBox<>(AccountType.values()))
                .setOpaque(false)
                .build()
            );

            panel.setOpaque(false);
            mainPanel.add(panel);
        }

        mainPanel.setOpaque(false);
        window.getContentPane().add(mainPanel);

        refresh();
    }

    public static void main(String[] args) {
        window.setMinimumSize(new Dimension(640, 480));
        window.setPreferredSize(new Dimension(843, 600));
        window.setLocationRelativeTo(null);
        window.setVisible(false);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Sets default colours for each component
        // Key reference: https://alvinalexander.com/java/java-uimanager-color-keys-list/
        UIManager.getDefaults().put("Label.foreground", Color.WHITE);

        window.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                var g2d = (Graphics2D) g;
                // Makes the rendering use "quality" rendering
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                var width = this.getWidth();
                var height = this.getHeight();
                var gradient = new GradientPaint(0f, 0f, ColorUtils.fromHex(0x2c70f0), 0f, height, ColorUtils.fromHex(0x25358e));

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);

                //super.paintComponent(g);
            }
        });

        Admin.init();
        Customer.init();
        Seller.init();

        createLogin();

        window.setVisible(true);
    }
}
