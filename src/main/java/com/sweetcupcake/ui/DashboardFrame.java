package com.sweetcupcake.ui;

import com.sweetcupcake.model.User;
import com.sweetcupcake.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {
    private JPanel contentArea;
    private CardLayout cardLayout;
    private User currentUser;
    private JLabel userInfoLabel;

    // Panel keys
    static final String PANEL_VIEW   = "VIEW_CUPCAKES";
    static final String PANEL_ADD    = "ADD_CUPCAKE";
    static final String PANEL_SEARCH = "SEARCH_CUPCAKES";
    static final String PANEL_USERS  = "MANAGE_USERS";

    public DashboardFrame() {
        currentUser = AuthService.getInstance().getCurrentUser();
        setTitle("The Sweet Cupcake Shop — Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 740);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_PRIMARY);
        setContentPane(root);

        // Sidebar
        JPanel sidebar = buildSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // Content
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UITheme.BG_PRIMARY);

        contentArea.add(new ViewCupcakesPanel(), PANEL_VIEW);
        contentArea.add(new AddCupcakePanel(this), PANEL_ADD);
        contentArea.add(new SearchCupcakesPanel(), PANEL_SEARCH);
        if (currentUser.isManager()) {
            contentArea.add(new ManageUsersPanel(), PANEL_USERS);
        }

        root.add(contentArea, BorderLayout.CENTER);
        cardLayout.show(contentArea, PANEL_VIEW);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, UITheme.BG_SIDEBAR, 0, getHeight(), UITheme.BG_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Brand header
        JPanel brandArea = new JPanel();
        brandArea.setLayout(new BoxLayout(brandArea, BoxLayout.Y_AXIS));
        brandArea.setOpaque(false);
        brandArea.setBorder(BorderFactory.createEmptyBorder(30, 20, 24, 20));
        brandArea.setMaximumSize(new Dimension(230, 120));

        JLabel iconLbl = new JLabel("🧁");
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandArea.add(iconLbl);

        JLabel nameLbl = new JLabel("Sweet Cupcake");
        nameLbl.setFont(UITheme.fontTitle(15));
        nameLbl.setForeground(UITheme.TEXT_WHITE);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandArea.add(nameLbl);

        JLabel shopLbl = new JLabel("Management System");
        shopLbl.setFont(UITheme.fontRegular(11));
        shopLbl.setForeground(new Color(255,255,255,140));
        shopLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandArea.add(shopLbl);

        sidebar.add(brandArea);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255,255,255,40));
        sep.setMaximumSize(new Dimension(230, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(16));

        // User info
        JPanel userBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        userBox.setOpaque(false);
        userBox.setMaximumSize(new Dimension(230, 60));
        JLabel avatarLbl = new JLabel(currentUser.isManager() ? "👑" : "👤");
        avatarLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        JPanel userText = new JPanel();
        userText.setOpaque(false);
        userText.setLayout(new BoxLayout(userText, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(currentUser.getFullName());
        userName.setFont(UITheme.fontBold(12));
        userName.setForeground(UITheme.TEXT_WHITE);
        JLabel userRole = new JLabel(currentUser.getRole().name());
        userRole.setFont(UITheme.fontRegular(10));
        userRole.setForeground(UITheme.ACCENT_ROSE);
        userText.add(userName);
        userText.add(userRole);
        userBox.add(avatarLbl);
        userBox.add(userText);
        sidebar.add(userBox);
        sidebar.add(Box.createVerticalStrut(12));

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(255,255,255,40));
        sep2.setMaximumSize(new Dimension(230, 1));
        sidebar.add(sep2);
        sidebar.add(Box.createVerticalStrut(16));

        // Navigation label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setFont(UITheme.fontBold(10));
        navLabel.setForeground(new Color(255,255,255,80));
        navLabel.setBorder(BorderFactory.createEmptyBorder(0,20,8,0));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navLabel);

        // Nav items
        addNavItem(sidebar, "🧁  View All Cupcakes",  PANEL_VIEW,   true);
        addNavItem(sidebar, "➕  Add New Cupcake",    PANEL_ADD,    false);
        addNavItem(sidebar, "🔍  Search Cupcakes",    PANEL_SEARCH, false);

        if (currentUser.isManager()) {
            sidebar.add(Box.createVerticalStrut(10));
            JLabel mgmtLabel = new JLabel("MANAGER");
            mgmtLabel.setFont(UITheme.fontBold(10));
            mgmtLabel.setForeground(UITheme.ACCENT_GOLD);
            mgmtLabel.setBorder(BorderFactory.createEmptyBorder(0,20,8,0));
            mgmtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(mgmtLabel);
            addNavItem(sidebar, "👥  Manage Users", PANEL_USERS, false);
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout button
        JButton logoutBtn = new JButton("⏻  Sign Out") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 60, 60, 100));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoutBtn.setForeground(new Color(255,100,100));
        logoutBtn.setFont(UITheme.fontBold(13));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        logoutBtn.setMaximumSize(new Dimension(230, 44));
        logoutBtn.addActionListener(e -> {
            AuthService.getInstance().logout();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });

        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    private void addNavItem(JPanel parent, String text, String panelKey, boolean selected) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected() || hovered) {
                    g2.setColor(new Color(255,255,255,isSelected()?30:15));
                    g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 8, 8);
                }
                if (isSelected()) {
                    g2.setColor(UITheme.ACCENT_PINK);
                    g2.fillRoundRect(0, 8, 4, getHeight()-16, 4, 4);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(selected ? UITheme.TEXT_WHITE : new Color(255,255,255,180));
        btn.setFont(selected ? UITheme.fontBold(13) : UITheme.fontRegular(13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(230, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        btn.addActionListener(e -> {
            // Reset all nav items appearance
            for (Component c : parent.getComponents()) {
                if (c instanceof JButton b) {
                    if (b != logoutRef(parent)) {
                        b.setFont(UITheme.fontRegular(13));
                        b.setForeground(new Color(255,255,255,180));
                        b.setSelected(false);
                    }
                }
            }
            btn.setFont(UITheme.fontBold(13));
            btn.setForeground(UITheme.TEXT_WHITE);
            btn.setSelected(true);
            cardLayout.show(contentArea, panelKey);
            // Refresh panels
            for (Component c : contentArea.getComponents()) {
                if (c.isVisible() && c instanceof RefreshablePanel rp) {
                    rp.refresh();
                }
            }
        });
        btn.setSelected(selected);
        parent.add(btn);
    }

    private JButton logoutRef(JPanel parent) {
        for (int i = parent.getComponentCount()-1; i >= 0; i--) {
            if (parent.getComponent(i) instanceof JButton b && b.getText().contains("Sign Out")) return b;
        }
        return null;
    }

    public void navigateTo(String panel) {
        cardLayout.show(contentArea, panel);
    }

    interface RefreshablePanel { void refresh(); }
}
