package app.customer;

import app.Main;
import app.auth.Account;
import app.auth.AccountType;
import app.auth.AuthManager;
import app.product.OrderManager;
import app.product.Product;
import app.product.ProductManager;
import app.seller.Seller;
import app.ui.*;
import app.util.ColorUtils;
import app.util.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Customer {
    private static final AuthManager authManager = new AuthManager(AccountType.CUSTOMER);

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {

    }

    public static void create(Account account) {
        Main.reset();

        var window = Main.getFrame();

        var mainPanel = new JPanel();
        mainPanel.setOpaque(false);

        var cartButton = new JButton(new ImageIcon(Utils.resizeImage("shopping_cart.png", 24, 24)));

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            panel.add(Utils.make(new JButton(account.getDisplayName(), new ImageIcon(Utils.getCircularImage(Utils.resizeImage("profile.png", 24, 24)))), button -> {
                button.setOpaque(false);
                button.setToolTipText("View Account Details");

                button.addActionListener(e -> {
                    showAccountDetailsScreen(account);
                });
            }));

            panel.add(Utils.make(cartButton, button -> {
                button.setText("0x");
                button.setToolTipText("View Shopping Cart");

                button.addActionListener(e -> {
                    // TODO: open Shopping Cart view
                });
            }));

            mainPanel.add(panel);
        }

        {
            var contentPanel = new ScrollablePanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);

            var mainScrollPane = new JScrollPane(contentPanel);
            mainScrollPane.setBorder(new LineBorder(new Color(0f, 0f, 0f, 0.2f), 1));
            mainScrollPane.setOpaque(false);

            mainScrollPane.setPreferredSize(new Dimension(745, 703));
            mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            for (Iterator<Product> it = ProductManager.getInstance().products(); it.hasNext();) {
                var product = it.next();
                var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                panel.setPreferredSize(new Dimension(window.getWidth() - 100, 60));
                panel.setMaximumSize(new Dimension(window.getWidth() - 100, 60));
                panel.setBackground(ColorUtils.fromHex(0x0047D6));
                panel.setBorder(new LineBorder(Color.BLACK, 1, true));

                var infoPanel = new JPanel();
                infoPanel.setOpaque(false);
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.add(new JLabel(product.getName()));
                infoPanel.add(new JLabel("EAN: " + product.getBarcode()));
                infoPanel.add(Utils.make(new JLabel("Seller: " + Seller.getAuthManager().getAccountByUUID(product.getSeller()).getDisplayName()), label -> {
                    label.setFont(label.getFont().deriveFont(11.5f));
                }));

                panel.add(infoPanel);

                var scrollPane = new JScrollPane(Utils.make(new MultilineTextLabel(), pane -> {
                    pane.setBorder(new EmptyBorder(0, 2, 2, 2));
                    pane.setForeground(Color.WHITE);
                    pane.setText(product.getDescription());
                }));
                scrollPane.setBorder(new CompoundBorder(
                    new EmptyBorder(0, 3, 2, 3),
                    new LineBorder(new Color(0f, 0f, 0f, 0.45f), 1)
                ));
                scrollPane.getViewport().setBackground(new Color(0f, 0f, 0f, 0.2f));
                scrollPane.setOpaque(false);
                scrollPane.setPreferredSize(new Dimension(400, 50));
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                panel.add(scrollPane);

                var numbersPanel = new JPanel();
                numbersPanel.setOpaque(false);
                numbersPanel.setLayout(new BoxLayout(numbersPanel, BoxLayout.Y_AXIS));

                var pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                pricePanel.setOpaque(false);
                if (product.getDiscount() > 0) {
                    pricePanel.add(new JLabel("Price: RM " + product.getPriceWithDiscount()));
                    pricePanel.add(Utils.make(new JLabel(" RM " + product.getPrice()), label -> {
                        Map attributes = label.getFont().getAttributes();
                        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);

                        label.setFont(new Font(attributes));
                    }));
                } else {
                    pricePanel.add(new JLabel("Price: RM " + product.getPrice()));
                }

                numbersPanel.add(pricePanel);
                numbersPanel.add(Utils.make(new JLabel(product.getStock() + " in stock"), label -> {
                    if (product.getStock() <= 0) // Display red if out of stock.
                        label.setForeground(Color.RED);
                }));

                panel.add(numbersPanel);

                var spinner = new JSpinner(new SpinnerNumberModel(Math.min(1, product.getStock()), 0, product.getStock(), 1));

                panel.add(spinner);

                panel.add(Utils.make(new JButton(new ImageIcon(Utils.resizeImage("shopping_cart.png", 24, 24))), button -> {
                    button.setPreferredSize(new Dimension(24, 24));
                    button.setToolTipText("Add to Cart");

                    // If the item is out of stock, the button should be disabled.
                    button.setEnabled((int) spinner.getValue() > 0 && (int) spinner.getValue() <= product.getStock());

                    spinner.addChangeListener(e -> {
                        button.setEnabled((int) spinner.getValue() > 0 && (int) spinner.getValue() <= product.getStock());
                    });

                    button.addActionListener(e -> {
                        OrderManager.getInstance().addToCart(account.getUUID(), product, (int) spinner.getValue());
                        spinner.setValue(Math.min(1, product.getStock())); // Reset value back to 1
                    });
                }));

                contentPanel.add(panel);
            }

            mainPanel.add(mainScrollPane);
        }

        window.getContentPane().add(mainPanel);

        window.setPreferredSize(new Dimension(1024, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        Main.refresh();
    }

    // Account Details screen
    public static void showAccountDetailsScreen(Account account) {
        Main.reset();

        var window = Main.getFrame();

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

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

        var email = new PlaceholderTextField("E-mail");
        var displayName = new PlaceholderTextField("Display Name");

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(new JLabel("Email"));
            panel.add(
                Utils.make(email, field -> {
                    field.setText(account.getEmail());
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            panel.add(new JLabel("Display Name"));
            panel.add(
                Utils.make(displayName, field -> {
                    field.setText(account.getDisplayName());
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            panel.add(Utils.make(new JPanel(new FlowLayout(FlowLayout.CENTER)), changePassword -> {
                changePassword.setOpaque(false);
                changePassword.add(Utils.make(new JButton("Change Password"), button -> {
                    ComponentHelper.makeHyperlink(button);
                    button.addActionListener(e -> showChangePasswordScreen(account));
                }));
            }));

            panel.add(
                Utils.make(new JButton("Save"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    button.setEnabled(false);

                    button.addActionListener(e -> {
                        AuthManager authManager = getAuthManager();
                        account.setEmail(email.getText());
                        account.setDisplayName(displayName.getText());

                        authManager.save();
                    });

                    var changeEvent = (ActionListener) e -> {
                        // Disable button if the email or display name is blank - we do not allow empty emails or display names.
                        if (email.getText().isBlank() || displayName.getText().isBlank())
                            button.setEnabled(false);
                        else if (email.getText().equals(account.getEmail()) && displayName.getText().equals(account.getDisplayName()))
                            button.setEnabled(false); // If they're the same, don't enable the Save button
                        else
                            button.setEnabled(true); // Otherwise just allow the button to be enabled
                    };

                    // Add the shared action event to both email and display name
                    email.addActionListener(changeEvent);
                    displayName.addActionListener(changeEvent);
                })
            );

            panel.add(
                Utils.make(new JButton("Exit"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);

                    button.addActionListener(e -> {
                        create(account);
                    });
                })
            );

            mainPanel.add(panel);
        }

        window.getContentPane().add(mainPanel);

        window.setPreferredSize(new Dimension(1024, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        Main.refresh();
    }

    // Change Password screen
    public static void showChangePasswordScreen(Account account) {
        Main.reset();

        var window = Main.getFrame();

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        var oldPassword = new PlaceholderPasswordTextField("Old Password");
        var newPassword = new PlaceholderPasswordTextField("New Password");
        var confirmNewPassword = new PlaceholderPasswordTextField("Confirm New Password");

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

            panel.add(new JLabel("Changing password for " + account.getDisplayName()));

            panel.setOpaque(false);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(
                Utils.make(oldPassword, field -> {
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            panel.add(
                Utils.make(newPassword, field -> {
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            panel.add(
                Utils.make(confirmNewPassword, field -> {
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            mainPanel.add(panel);
        }

        var errorText = new JLabel("Error: [unknown]");
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
                Utils.make(new JButton("Save"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    button.setEnabled(false);

                    button.addActionListener(e -> {
                        AuthManager authManager = getAuthManager();
                        try {
                            authManager.changePassword(account, oldPassword.getText(), newPassword.getText());
                        } catch (Exception exception) {
                            errorText.setText("Error: " + exception.getMessage());
                            errorText.setVisible(true);
                            exception.printStackTrace();
                        }

                        showAccountDetailsScreen(account);
                    });

                    var changeEvent = (ActionListener) e -> {
                        // Disable button if the email or display name is blank - we do not allow empty emails or display names.
                        if (oldPassword.getText().isBlank() || newPassword.getText().isBlank() || confirmNewPassword.getText().isBlank())
                            button.setEnabled(false);
                        else
                            button.setEnabled(newPassword.getText().equals(confirmNewPassword.getText())); // If they're the same, enable the Save button
                    };

                    // Add the shared action event to all password fields
                    oldPassword.addActionListener(changeEvent);
                    newPassword.addActionListener(changeEvent);
                    confirmNewPassword.addActionListener(changeEvent);
                })
            );

            panel.add(
                Utils.make(new JButton("Exit"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);

                    button.addActionListener(e -> {
                        showAccountDetailsScreen(account);
                    });
                })
            );

            mainPanel.add(panel);
        }

        window.getContentPane().add(mainPanel);

        window.setPreferredSize(new Dimension(1024, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        Main.refresh();
    }
}
