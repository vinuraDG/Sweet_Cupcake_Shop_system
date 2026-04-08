package com.sweetcupcake.ui;

import com.sweetcupcake.model.User;
import com.sweetcupcake.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginFrame() {
        setTitle("The Sweet Cupcake Shop — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(880, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_PRIMARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(root);

        // Left decorative panel
        JPanel leftPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, UITheme.BG_DARK, 0, getHeight(), new Color(0x6B, 0x21, 0x35));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative circles
                g2.setColor(new Color(255,255,255,15));
                g2.fillOval(-60, -60, 250, 250);
                g2.fillOval(getWidth()-120, getHeight()-150, 280, 280);
                g2.setColor(new Color(255,255,255,10));
                g2.fillOval(20, getHeight()/2-80, 160, 160);
                // Cupcake emoji art area
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setFont(UITheme.fontTitle(80));
                g2.drawString("🧁", getWidth()/2 - 50, getHeight()/2 - 20);
            }
        };
        leftPanel.setPreferredSize(new Dimension(340, 0));
        leftPanel.setLayout(new GridBagLayout());

        JPanel brandPanel = new JPanel(new GridBagLayout());
        brandPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel icon = new JLabel("🧁");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        gbc.gridy = 0; gbc.insets = new Insets(0,0,16,0);
        brandPanel.add(icon, gbc);

        JLabel brand = new JLabel("<html><center>The Sweet<br>Cupcake Shop</center></html>");
        brand.setFont(UITheme.fontTitle(22));
        brand.setForeground(UITheme.TEXT_WHITE);
        brand.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1; gbc.insets = new Insets(0,0,8,0);
        brandPanel.add(brand, gbc);

        JLabel tagline = new JLabel("Sweet Moments, Every Bite");
        tagline.setFont(UITheme.fontItalic(13));
        tagline.setForeground(new Color(255,255,255,180));
        gbc.gridy = 2; gbc.insets = new Insets(0,0,0,0);
        brandPanel.add(tagline, gbc);

        leftPanel.add(brandPanel);

        // Right login form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UITheme.BG_PRIMARY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(UITheme.BG_CARD);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new com.sweetcupcake.ui.RoundedBorder(16, new Color(0xFF, 0xD8, 0xCC)),
            BorderFactory.createEmptyBorder(36, 40, 36, 40)
        ));

        // Welcome text
        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(UITheme.fontTitle(22));
        welcome.setForeground(UITheme.TEXT_PRIMARY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(welcome);
        formCard.add(Box.createVerticalStrut(6));

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(UITheme.fontItalic(13));
        subtitle.setForeground(UITheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(subtitle);
        formCard.add(Box.createVerticalStrut(28));

        // Username
        JLabel userLbl = UITheme.sectionLabel("Username");
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(userLbl);
        formCard.add(Box.createVerticalStrut(6));
        usernameField = UITheme.styledTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(usernameField);
        formCard.add(Box.createVerticalStrut(16));

        // Password
        JLabel passLbl = UITheme.sectionLabel("Password");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passLbl);
        formCard.add(Box.createVerticalStrut(6));
        passwordField = UITheme.styledPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(passwordField);
        formCard.add(Box.createVerticalStrut(8));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.fontRegular(12));
        errorLabel.setForeground(UITheme.ERROR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(errorLabel);
        formCard.add(Box.createVerticalStrut(20));

        // Login button
        JButton loginBtn = UITheme.primaryButton("Sign In  →");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> attemptLogin());
        formCard.add(loginBtn);

        // Enter key
        passwordField.addActionListener(e -> attemptLogin());
        usernameField.addActionListener(e -> passwordField.requestFocus());

        rightPanel.add(formCard);

        // Hint
        JPanel hint = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        hint.setOpaque(false);
        JLabel hintLabel = new JLabel("Default: admin / admin123");
        hintLabel.setFont(UITheme.fontRegular(11));
        hintLabel.setForeground(UITheme.TEXT_SECONDARY);
        hint.add(hintLabel);

        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setBackground(UITheme.BG_PRIMARY);
        rightWrapper.add(rightPanel, BorderLayout.CENTER);
        rightWrapper.add(hint, BorderLayout.SOUTH);
        rightWrapper.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        root.add(leftPanel, BorderLayout.WEST);
        root.add(rightWrapper, BorderLayout.CENTER);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        User user = AuthService.getInstance().login(username, password);
        if (user != null) {
            dispose();
            SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
        } else {
            errorLabel.setText("Invalid username or password.");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

}
