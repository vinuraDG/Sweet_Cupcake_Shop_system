package com.sweetcupcake.ui;

import com.sweetcupcake.service.CupcakeService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddCupcakePanel extends JPanel {
    private JTextField nameField, flavorField, priceField, descField;
    private JComboBox<String> categoryBox;
    private JLabel statusLabel;
    private final CupcakeService service = CupcakeService.getInstance();
    private final DashboardFrame parent;

    private static final String[] DEFAULT_CATEGORIES = {
        "Classic Flavors", "Fruity Delights", "Seasonal Specials",
        "Gluten-Free", "Custom Orders", "Vegan", "Kids Special", "Other"
    };

    public AddCupcakePanel(DashboardFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_PRIMARY);
        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xFF, 0xE0, 0xD0)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JLabel title = UITheme.titleLabel("➕  Add New Cupcake");
        header.add(title, BorderLayout.WEST);
        JLabel sub = new JLabel("Fill in the details to add a new cupcake to the menu");
        sub.setFont(UITheme.fontItalic(13));
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setBorder(BorderFactory.createEmptyBorder(4,0,0,0));
        header.add(sub, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Form area
        JPanel formArea = new JPanel(new GridBagLayout());
        formArea.setBackground(UITheme.BG_PRIMARY);
        formArea.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(0xFF, 0xD0, 0xC0)),
            BorderFactory.createEmptyBorder(30, 36, 30, 36)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        card.add(UITheme.sectionLabel("Cupcake Name *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = UITheme.styledTextField(25);
        nameField.setPreferredSize(new Dimension(280, 38));
        card.add(nameField, gbc);

        // Flavor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        card.add(UITheme.sectionLabel("Flavor *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        flavorField = UITheme.styledTextField(25);
        flavorField.setPreferredSize(new Dimension(280, 38));
        card.add(flavorField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        card.add(UITheme.sectionLabel("Category *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        categoryBox = new JComboBox<>(buildCategoryList());
        categoryBox.setFont(UITheme.fontRegular(13));
        categoryBox.setBackground(UITheme.BG_CARD);
        categoryBox.setForeground(UITheme.TEXT_PRIMARY);
        categoryBox.setPreferredSize(new Dimension(280, 38));
        card.add(categoryBox, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        card.add(UITheme.sectionLabel("Price ($) *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        priceField = UITheme.styledTextField(10);
        priceField.setPreferredSize(new Dimension(280, 38));
        priceField.setToolTipText("e.g. 2.99");
        card.add(priceField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.NORTH;
        card.add(UITheme.sectionLabel("Description"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.anchor = GridBagConstraints.CENTER;
        descField = UITheme.styledTextField(25);
        descField.setPreferredSize(new Dimension(280, 38));
        card.add(descField, gbc);

        // Status label
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.insets = new Insets(12, 8, 0, 8);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.fontBold(13));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(statusLabel, gbc);

        // Buttons
        gbc.gridy = 6; gbc.insets = new Insets(8, 8, 8, 8);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btnRow.setOpaque(false);

        JButton saveBtn = UITheme.primaryButton("💾  Save Cupcake");
        saveBtn.setPreferredSize(new Dimension(180, 42));
        saveBtn.addActionListener(e -> saveCupcake());

        JButton clearBtn = UITheme.secondaryButton("✕  Clear Form");
        clearBtn.setPreferredSize(new Dimension(140, 42));
        clearBtn.addActionListener(e -> clearForm());

        btnRow.add(saveBtn);
        btnRow.add(clearBtn);
        card.add(btnRow, gbc);

        formArea.add(card);
        add(formArea, BorderLayout.CENTER);
    }

    private String[] buildCategoryList() {
        List<String> existing = service.getAllCategories();
        java.util.Set<String> all = new java.util.LinkedHashSet<>(existing);
        for (String d : DEFAULT_CATEGORIES) all.add(d);
        return all.toArray(new String[0]);
    }

    private void saveCupcake() {
        String name = nameField.getText().trim();
        String flavor = flavorField.getText().trim();
        String category = (String) categoryBox.getSelectedItem();
        String priceStr = priceField.getText().trim();
        String desc = descField.getText().trim();

        if (name.isEmpty() || flavor.isEmpty() || priceStr.isEmpty()) {
            showStatus("⚠ Please fill in all required fields.", UITheme.WARNING);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showStatus("⚠ Please enter a valid price.", UITheme.ERROR);
            return;
        }

        boolean success = service.addCupcake(name, flavor, category, price, desc);
        if (success) {
            showStatus("✓ Cupcake \"" + name + "\" added successfully!", UITheme.SUCCESS);
            clearForm();
        } else {
            showStatus("✗ Failed to add cupcake. Please try again.", UITheme.ERROR);
        }
    }

    private void clearForm() {
        nameField.setText("");
        flavorField.setText("");
        priceField.setText("");
        descField.setText("");
        categoryBox.setSelectedIndex(0);
    }

    private void showStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
        Timer t = new Timer(4000, e -> statusLabel.setText(" "));
        t.setRepeats(false);
        t.start();
    }
}
