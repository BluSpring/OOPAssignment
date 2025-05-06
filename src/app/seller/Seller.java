package app.seller;

import app.Main;
import app.auth.Account;
import app.auth.AccountType;
import app.auth.AuthManager;
import app.ui.ComponentHelper;
import app.ui.GraphBuilder;
import app.util.ColorUtils;
import app.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Seller {
    private static final AuthManager authManager = new AuthManager(AccountType.SELLER);

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {

    }

    public static void create(Account account) {
        Main.reset();

        var window = Main.getFrame();

        window.setPreferredSize(new Dimension(1024, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        var mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(createSidebar(), BorderLayout.LINE_START);

        var contentsPanel = new JPanel(new GridLayout(2, 2));
        contentsPanel.setOpaque(false);
        // Business Insights
        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            panel.add(new JLabel("Business Insights"));

            var graph = new GraphBuilder(450, 250, Color.YELLOW, ColorUtils.fromHex(0x404040), ColorUtils.fromHex(0xADADAD));
            panel.add(Utils.make(new JLabel(new ImageIcon(graph.createImage())), component -> {
                component.setBorder(new EmptyBorder(5, 5, 5, 5));
            }));

            contentsPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.add(new JLabel("Opening Campaigns"));

            var innerPanel = new JPanel();
            panel.add(innerPanel);

            contentsPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.add(new JLabel("Announcements"));

            var innerPanel = new JPanel();
            panel.add(innerPanel);

            contentsPanel.add(panel);
        }

        mainPanel.add(contentsPanel);
        window.getContentPane().add(mainPanel);

        Main.refresh();
    }

    private static JPanel createSidebar() {
        var sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(192, Main.getFrame().getHeight()));
        sidebar.setAlignmentX(0f);
        sidebar.setLocation(0, 0);
        sidebar.setOpaque(false);
        //sidebar.setBackground(ColorUtils.fromHex(0x2F70D8));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Order panel
        {
            sidebar.add(makeDropdownPanel("Orders", List.of(
                new DropdownItem("View Orders", () -> {}),
                new DropdownItem("View Reviews", () -> {})
            )));

            sidebar.add(makeDropdownPanel("Products", List.of(
                new DropdownItem("View Products", () -> {}),
                new DropdownItem("Add/Remove Products", () -> {})
            )));
        }

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
