package com.sweetcupcake.ui;

import com.sweetcupcake.model.User;
import com.sweetcupcake.service.AuthService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ManageUsersPanel extends JPanel implements DashboardFrame.RefreshablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField newUsername, newFullName;
    private JPasswordField newPassword;
    private JLabel statusLabel;
    private JLabel userCountLabel;
    private final AuthService authService = AuthService.getInstance();

    public ManageUsersPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_PRIMARY);
        initUI();
        loadUsers();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xFF, 0xE0, 0xD0)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel title = UITheme.titleLabel("👥  Manage Users");
        titleRow.add(title, BorderLayout.WEST);
        userCountLabel = new JLabel("");
        userCountLabel.setFont(UITheme.fontItalic(13));
        userCountLabel.setForeground(UITheme.TEXT_SECONDARY);
        titleRow.add(userCountLabel, BorderLayout.EAST);
        header.add(titleRow, BorderLayout.NORTH);
        JLabel sub = new JLabel("Create cashier accounts and view all system users");
        sub.setFont(UITheme.fontItalic(13));
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setBorder(BorderFactory.createEmptyBorder(4,0,0,0));
        header.add(sub, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Split layout
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setBackground(UITheme.BG_PRIMARY);
        split.setDividerSize(1);
        split.setDividerLocation(480);

        // Left: user table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UITheme.BG_PRIMARY);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 14));

        JLabel tblTitle = UITheme.sectionLabel("All System Users");
        tblTitle.setBorder(BorderFactory.createEmptyBorder(0,0,12,0));
        tablePanel.add(tblTitle, BorderLayout.NORTH);

        String[] cols = {"#", "Full Name", "Username", "Role", "Created"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.setupTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(3).setMaxWidth(100);

        // Role badge renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(" " + val + " ", SwingConstants.CENTER);
                lbl.setFont(UITheme.fontBold(11));
                lbl.setOpaque(true);
                boolean isManager = "MANAGER".equals(val);
                lbl.setBackground(sel ? UITheme.BG_SECONDARY : (isManager ? new Color(0xFF, 0xF0, 0xD0) : new Color(0xE3, 0xF2, 0xFD)));
                lbl.setForeground(isManager ? UITheme.ACCENT_GOLD : new Color(0x18, 0x65, 0xC3));
                return lbl;
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) comp.setBackground(r%2==0 ? UITheme.BG_CARD : UITheme.BG_PRIMARY);
                if (!sel) comp.setForeground(UITheme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                return comp;
            }
        });
        // Reapply role renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(" " + val + " ", SwingConstants.CENTER);
                lbl.setFont(UITheme.fontBold(11));
                lbl.setOpaque(true);
                boolean isMgr = "MANAGER".equals(val);
                lbl.setBackground(sel ? UITheme.BG_SECONDARY : (isMgr ? new Color(0xFF, 0xF0, 0xD0) : new Color(0xE3, 0xF2, 0xFD)));
                lbl.setForeground(isMgr ? UITheme.ACCENT_GOLD : new Color(0x18, 0x65, 0xC3));
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        tablePanel.add(scroll, BorderLayout.CENTER);

        split.setLeftComponent(tablePanel);

        // Right: create cashier form
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(UITheme.BG_PRIMARY);
        formPanel.setBorder(BorderFactory.createEmptyBorder(24, 14, 24, 28));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(UITheme.BG_CARD);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, new Color(0xFF, 0xD0, 0xC0)),
            BorderFactory.createEmptyBorder(28, 30, 28, 30)
        ));

        JLabel formTitle = new JLabel("Create New Cashier");
        formTitle.setFont(UITheme.fontTitle(17));
        formTitle.setForeground(UITheme.TEXT_PRIMARY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(4));

        JLabel formSub = new JLabel("Add a new cashier account to the system");
        formSub.setFont(UITheme.fontItalic(12));
        formSub.setForeground(UITheme.TEXT_SECONDARY);
        formSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formSub);
        formCard.add(Box.createVerticalStrut(24));

        // Full Name
        addFormRow(formCard, "Full Name *");
        newFullName = UITheme.styledTextField(20);
        newFullName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        newFullName.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(newFullName);
        formCard.add(Box.createVerticalStrut(14));

        // Username
        addFormRow(formCard, "Username *");
        newUsername = UITheme.styledTextField(20);
        newUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        newUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(newUsername);
        formCard.add(Box.createVerticalStrut(14));

        // Password
        addFormRow(formCard, "Password *");
        newPassword = UITheme.styledPasswordField(20);
        newPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        newPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(newPassword);
        formCard.add(Box.createVerticalStrut(8));

        // Status
        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.fontBold(12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(statusLabel);
        formCard.add(Box.createVerticalStrut(18));

        // Create button
        JButton createBtn = UITheme.primaryButton("👤  Create Cashier");
        createBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        createBtn.addActionListener(e -> createCashier());
        formCard.add(createBtn);

        // Permissions info
        formCard.add(Box.createVerticalStrut(24));
        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(new Color(0xFF, 0xF5, 0xF0));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, new Color(0xFF, 0xD0, 0xC0)),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        JLabel infoTitle = new JLabel("🔒 Cashier Permissions:");
        infoTitle.setFont(UITheme.fontBold(11));
        infoTitle.setForeground(UITheme.TEXT_SECONDARY);
        JLabel p1 = new JLabel("  ✓  View all cupcakes");
        JLabel p2 = new JLabel("  ✓  Add new cupcakes");
        JLabel p3 = new JLabel("  ✓  Search & filter cupcakes");
        for (JLabel l : new JLabel[]{infoTitle,p1,p2,p3}) {
            l.setFont(l == infoTitle ? UITheme.fontBold(11) : UITheme.fontRegular(11));
            l.setForeground(UITheme.TEXT_SECONDARY);
            infoBox.add(l);
        }
        formCard.add(infoBox);

        formPanel.add(formCard, BorderLayout.NORTH);
        split.setRightComponent(formPanel);

        JPanel splitWrapper = new JPanel(new BorderLayout());
        splitWrapper.setBackground(UITheme.BG_PRIMARY);
        splitWrapper.add(split, BorderLayout.CENTER);
        add(splitWrapper, BorderLayout.CENTER);
    }

    private void addFormRow(JPanel panel, String labelText) {
        JLabel lbl = UITheme.sectionLabel(labelText);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
    }

    private void createCashier() {
        String fullName = newFullName.getText().trim();
        String username = newUsername.getText().trim();
        String password = new String(newPassword.getPassword()).trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showStatus("⚠ All fields are required.", UITheme.WARNING);
            return;
        }
        if (username.length() < 4) {
            showStatus("⚠ Username must be at least 4 characters.", UITheme.WARNING);
            return;
        }
        if (password.length() < 6) {
            showStatus("⚠ Password must be at least 6 characters.", UITheme.WARNING);
            return;
        }

        boolean success = authService.createCashier(username, password, fullName);
        if (success) {
            showStatus("✓ Cashier \"" + fullName + "\" created!", UITheme.SUCCESS);
            newFullName.setText(""); newUsername.setText(""); newPassword.setText("");
            loadUsers();
        } else {
            showStatus("✗ Username already exists or error occurred.", UITheme.ERROR);
        }
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = authService.getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            String date = u.getCreatedAt() != null && u.getCreatedAt().length() >= 10
                ? u.getCreatedAt().substring(0, 10) : "—";
            tableModel.addRow(new Object[]{i+1, u.getFullName(), u.getUsername(), u.getRole().name(), date});
        }
        userCountLabel.setText(users.size() + " users");
    }

    private void showStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
        Timer t = new Timer(4000, e -> statusLabel.setText(" "));
        t.setRepeats(false);
        t.start();
    }

    @Override public void refresh() { loadUsers(); }
}
