package app.customer;

import app.Main;
import app.auth.Account;
import app.auth.AccountType;
import app.auth.AuthManager;
import app.product.Product;
import app.product.ProductManager;
import app.seller.Seller;
import app.ui.MultilineTextLabel;
import app.util.ColorUtils;
import app.util.Utils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
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

        {
            var contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);

            for (Iterator<Product> it = ProductManager.getInstance().products(); it.hasNext();) {
                var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                panel.setPreferredSize(new Dimension(window.getWidth() - 100, 60));
                panel.setBackground(ColorUtils.fromHex(0x0047D6));
                panel.setBorder(new LineBorder(Color.BLACK, 1, true));

                var product = it.next();

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
                numbersPanel.add(new JLabel(product.getStock() + " in stock"));

                panel.add(numbersPanel);

                contentPanel.add(panel);
            }

            mainPanel.add(contentPanel);
        }

        window.getContentPane().add(mainPanel);

        window.setPreferredSize(new Dimension(1024, 768));
        window.setLocationRelativeTo(null);
        window.pack();

        Main.refresh();
    }
}
