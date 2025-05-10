package app.admin;

import app.Main;
import app.auth.Account;
import app.product.Product;
import app.product.ProductCategory;
import app.product.ProductManager;
import app.seller.Seller;
import app.ui.ComponentHelper;
import app.ui.MultilineTextLabel;
import app.ui.ScrollablePanel;
import app.ui.SharedScreens;
import app.util.ColorUtils;
import app.util.Utils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdminViews {
    private static final int PANEL_WIDTH = 775;

    public static void createHomeScreen(Account account) {
        Main.reset();

        var window = Main.getFrame();
        window.setPreferredSize(new Dimension(1280, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        var mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setOpaque(false);
        mainPanel.add(createSidebar(account));

        var secondPanel = new JPanel();
        secondPanel.setOpaque(false);

        {

        }

        mainPanel.add(secondPanel);
        window.getContentPane().add(mainPanel);

        Main.refresh();
    }

    public static void createManageSellersScreen(Account account) {
        Main.reset();

        var window = Main.getFrame();
        window.setPreferredSize(new Dimension(1280, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        var mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setOpaque(false);
        mainPanel.add(createSidebar(account));

        var secondPanel = new JPanel();
        secondPanel.setOpaque(false);

        {

        }

        mainPanel.add(secondPanel);
        window.getContentPane().add(mainPanel);

        Main.refresh();

        Main.refresh();
    }

    public static void createManageCategoriesScreen(Account account) {
        Main.reset();

        var window = Main.getFrame();
        window.setPreferredSize(new Dimension(1280, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        var mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setOpaque(false);
        mainPanel.add(createSidebar(account));

        var secondPanel = new JPanel();
        secondPanel.setOpaque(false);

        {
            var contentPanel = new ScrollablePanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);

            var mainScrollPane = new JScrollPane(contentPanel);
            mainScrollPane.setBorder(new LineBorder(new Color(0f, 0f, 0f, 0.2f), 1));
            mainScrollPane.setOpaque(false);

            mainScrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 703));
            mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            for (Product product : ProductManager.getInstance().products()) {
                var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                panel.setPreferredSize(new Dimension(PANEL_WIDTH - 2, 80));
                panel.setMaximumSize(new Dimension(PANEL_WIDTH - 2, 80));
                panel.setBackground(ColorUtils.fromHex(0x0047D6));
                panel.setBorder(new LineBorder(Color.BLACK, 1, true));

                var infoPanel = new JPanel();
                infoPanel.setOpaque(false);
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.add(new JLabel(product.getName()));
                infoPanel.add(new JLabel("EAN: " + product.getBarcode()));
                infoPanel.add(Utils.make(new JComboBox<>(ProductCategory.values()), box -> {
                    box.addItemListener(e -> {
                        product.setCategory((ProductCategory) e.getItem());
                        ProductManager.getInstance().save();
                    });
                }));
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
                scrollPane.setPreferredSize(new Dimension(400, 60));
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

                contentPanel.add(panel);
            }

            mainPanel.add(mainScrollPane);
        }

        mainPanel.add(secondPanel);
        window.getContentPane().add(mainPanel);

        Main.refresh();

        Main.refresh();
    }

    public static JPanel createSidebar(Account account) {
        var sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(192, Main.getFrame().getHeight()));
        sidebar.setAlignmentX(0f);
        sidebar.setLocation(0, 0);
        sidebar.setOpaque(false);
        //sidebar.setBackground(ColorUtils.fromHex(0x2F70D8));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Sellers panel
        {
            sidebar.add(makeDropdownPanel("Administrative", java.util.List.of(
                new DropdownItem("Home", () -> createHomeScreen(account)),
                new DropdownItem("Manage Sellers", () -> createManageSellersScreen(account)),
                new DropdownItem("Manage Categories", () -> createManageCategoriesScreen(account))
            )));
        }

        sidebar.add(Utils.make(new JButton(account.getDisplayName(), new ImageIcon(Utils.getCircularImage(Utils.resizeImage("profile.png", 24, 24)))), button -> {
            button.setOpaque(false);
            button.setToolTipText("View Account Details");

            button.addActionListener(e -> {
                SharedScreens.showAccountDetailsScreen(Admin.getAuthManager(), account, () -> Admin.create(account));
            });
        }));

        return sidebar;
    }

    private record DropdownItem(String name, Runnable clickHandler) {}

    private static JPanel makeDropdownPanel(String name, List<DropdownItem> dropdownItems) {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        var isOpen = new AtomicBoolean(true);

        var subcontainer = new JPanel();
        subcontainer.setBorder(new EmptyBorder(2, 5, 5, 5));
        subcontainer.setLayout(new BoxLayout(subcontainer, BoxLayout.Y_AXIS));
        subcontainer.setOpaque(false);

        // ▲▼
        var button = new JButton(name + " ▲");
        button.setOpaque(false);
        button.setFont(button.getFont().deriveFont(16f));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(2, 2, 2, 2));
        button.setBackground(ColorUtils.NONE);
        button.addActionListener(e -> {
            if (isOpen.get()) {
                button.setText(name + " ▼");
                subcontainer.setVisible(false);
                isOpen.set(false);
            } else {
                button.setText(name + " ▲");
                subcontainer.setVisible(true);
                isOpen.set(true);
            }
        });

        panel.add(button);

        for (DropdownItem item : dropdownItems) {
            subcontainer.add(Utils.make(new JButton(item.name()), itemBtn -> {
                ComponentHelper.makeHyperlink(itemBtn);
                itemBtn.addActionListener(e -> item.clickHandler().run());
            }));
        }

        panel.add(subcontainer);
        return panel;
    }
}
