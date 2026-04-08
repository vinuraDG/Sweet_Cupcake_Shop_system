package com.sweetcupcake.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UITheme {
    // Color Palette — warm bakery aesthetic
    public static final Color BG_PRIMARY     = new Color(0xFF, 0xF5, 0xF0);  // warm cream
    public static final Color BG_SECONDARY   = new Color(0xFF, 0xE8, 0xE0);  // soft peach
    public static final Color BG_CARD        = Color.WHITE;
    public static final Color BG_DARK        = new Color(0x3D, 0x1A, 0x1A);  // deep chocolate
    public static final Color BG_SIDEBAR     = new Color(0x2E, 0x12, 0x12);  // darker chocolate

    public static final Color ACCENT_PINK    = new Color(0xE8, 0x4E, 0x89);  // hot pink
    public static final Color ACCENT_ROSE    = new Color(0xF4, 0x8F, 0xB1);  // rose
    public static final Color ACCENT_PEACH   = new Color(0xFF, 0xA0, 0x7A);  // peach
    public static final Color ACCENT_GOLD    = new Color(0xD4, 0xAF, 0x37);  // gold

    public static final Color TEXT_PRIMARY   = new Color(0x2C, 0x10, 0x10);  // dark
    public static final Color TEXT_SECONDARY = new Color(0x8B, 0x5A, 0x5A);  // muted
    public static final Color TEXT_WHITE     = Color.WHITE;
    public static final Color TEXT_LIGHT     = new Color(0xF5, 0xE6, 0xE6);

    public static final Color SUCCESS        = new Color(0x4C, 0xAF, 0x50);
    public static final Color ERROR          = new Color(0xE5, 0x39, 0x35);
    public static final Color WARNING        = new Color(0xFF, 0x98, 0x00);

    // Fonts
    public static Font fontTitle(float size)   { return new Font("Georgia", Font.BOLD, (int)size); }
    public static Font fontBold(float size)    { return new Font("Segoe UI", Font.BOLD, (int)size); }
    public static Font fontRegular(float size) { return new Font("Segoe UI", Font.PLAIN, (int)size); }
    public static Font fontItalic(float size)  { return new Font("Georgia", Font.ITALIC, (int)size); }

    // Borders
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0xFF, 0xD0, 0xC8), 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );
    }

    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0xE8, 0xC8, 0xC0), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
    }

    public static Border focusBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT_PINK, 2, true),
            BorderFactory.createEmptyBorder(5, 9, 5, 9)
        );
    }

    // Styled button factory
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0xC0, 0x3A, 0x70));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0xF5, 0x60, 0x9A));
                } else {
                    g2.setColor(ACCENT_PINK);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_WHITE);
        btn.setFont(fontBold(13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 38));
        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(BG_SECONDARY);
                } else {
                    g2.setColor(BG_CARD);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(ACCENT_PINK);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(ACCENT_PINK);
        btn.setFont(fontBold(13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 38));
        return btn;
    }

    public static JTextField styledTextField(int cols) {
        JTextField field = new JTextField(cols);
        field.setFont(fontRegular(13));
        field.setBackground(BG_CARD);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(inputBorder());
        field.setPreferredSize(new Dimension(200, 36));
        return field;
    }

    public static JPasswordField styledPasswordField(int cols) {
        JPasswordField field = new JPasswordField(cols);
        field.setFont(fontRegular(13));
        field.setBackground(BG_CARD);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(inputBorder());
        field.setPreferredSize(new Dimension(200, 36));
        return field;
    }

    public static JLabel titleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontTitle(24));
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    public static JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontBold(14));
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    public static void setupTable(JTable table) {
        table.setFont(fontRegular(13));
        table.setRowHeight(38);
        table.setGridColor(new Color(0xFF, 0xE8, 0xE0));
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(new Color(0xFF, 0xD0, 0xDC));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(10, 0));
        table.getTableHeader().setFont(fontBold(12));
        table.getTableHeader().setBackground(BG_SECONDARY);
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xFF, 0xC0, 0xC0)));
        table.setFocusable(false);
    }
}
