import admin.Admin;
import auth.AccountType;
import auth.AuthManager;
import customer.Customer;
import seller.Seller;
import ui.ComponentHelper;
import ui.PlaceholderTextField;
import util.ColorUtils;
import util.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

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

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        var accountType = new JComboBox<>(AccountType.values());
        var email = new PlaceholderTextField("E-mail");
        var password = new PlaceholderTextField("Password");

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            // not sure why Swing's not allowing us to resize anything, so we're just adding padding ourselves.
            panel.add(new JLabel(new ImageIcon(Utils.createEmptyImage(15, 18))));
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setOpaque(false);

            try {
                var image = Utils.getCircularImage(ImageIO.read(Main.class.getResourceAsStream("/images/profile.png")))
                    .getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                panel.add(new JLabel(new ImageIcon(image)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mainPanel.add(panel);
        }

        {
            var panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            panel.add(new JLabel("Login as: "));
            panel.add(accountType);

            panel.setOpaque(false);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(
                Utils.make(email, field -> {
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
                Utils.make(password, field -> {
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
                    ComponentHelper.makeHyperlink(button);
                })
            );

            mainPanel.add(panel);
        }

        var errorText = new JLabel("Error: Invalid email/password!");
        errorText.setForeground(ColorUtils.fromHex(0xFF5A5A));
        errorText.setFont(errorText.getFont().deriveFont(14f));

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
            errorText.setVisible(false);
            panel.add(errorText);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            panel.add(
                Utils.make(new JButton("Login"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);

                    button.addActionListener(e -> {
                        errorText.setVisible(false);

                        try {
                            AuthManager authManager = getAuthManager((AccountType) accountType.getSelectedItem());
                            var account = authManager.login(email.getText(), password.getText());

                            switch ((AccountType) Objects.requireNonNull(accountType.getSelectedItem())) {
                                case ADMINISTRATOR -> Admin.create(account);
                                case CUSTOMER -> Customer.create(account);
                                case SELLER -> Seller.create(account);
                            }
                        } catch (Exception exception) {
                            errorText.setText("Error: " + exception.getMessage());
                            errorText.setVisible(true);
                            exception.printStackTrace();
                        }
                    });
                })
            );

            panel.add(Utils.make(new JPanel(new FlowLayout(FlowLayout.CENTER)), signUp -> {
                signUp.setOpaque(false);
                signUp.add(new JLabel("Not a user? "));

                signUp.add(Utils.make(new JButton("Create an account"), button -> {
                    ComponentHelper.makeHyperlink(button);
                }));
            }));

            mainPanel.add(panel);
        }

        mainPanel.setOpaque(false);
        window.getContentPane().add(mainPanel);

        refresh();
    }

    public static AuthManager getAuthManager(AccountType type) {
        AuthManager authManager;

        switch (type) {
            case ADMINISTRATOR -> authManager = Admin.getAuthManager();
            case CUSTOMER -> authManager = Customer.getAuthManager();
            case SELLER -> authManager = Seller.getAuthManager();
            case null, default -> throw new IllegalStateException("Invalid account type provided!");
        }

        return authManager;
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
