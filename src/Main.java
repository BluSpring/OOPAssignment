import admin.Admin;
import auth.AccountType;
import customer.Customer;
import seller.Seller;
import ui.PlaceholderTextField;
import util.ColorUtils;
import util.SwingComponentBuilder;
import util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

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
                .build()
            );

            panel.setOpaque(false);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(
                Utils.make(new PlaceholderTextField("E-mail"), field -> {
                    field.setOpaque(false);
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(2, 2, 2, 2),
                            LineBorder.createBlackLineBorder()
                        ),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                    ));
                })
            );
            panel.add(
                Utils.make(new PlaceholderTextField("Password"), field -> {
                    field.setOpaque(false);
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(2, 2, 2, 2),
                            LineBorder.createBlackLineBorder()
                        ),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                    ));
                })
            );

            panel.setOpaque(false);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setOpaque(false);

            panel.add(
                Utils.make(new JCheckBox("Remember Me"), component -> {
                    component.setOpaque(false);
                    component.setForeground(Color.WHITE);
                    component.setBorder(new EmptyBorder(2, 2, 2, 2));
                })
            );

            panel.add(
                Utils.make(new JButton("Forgot password"), button -> {
                    button.setOpaque(false);

                    // Do not set the generic types, as otherwise an error occurs in compilation.
                    Map attributes = button.getFont().getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    button.setFont(button.getFont().deriveFont(attributes));

                    button.setForeground(Color.WHITE);
                    button.setBorder(new EmptyBorder(2, 2, 2, 2));
                    button.setBackground(ColorUtils.NONE);
                })
            );

            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            panel.add(
                Utils.make(new JButton("Login"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                })
            );

            panel.add(
                Utils.make(new JButton("Sign Up"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                })
            );

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
