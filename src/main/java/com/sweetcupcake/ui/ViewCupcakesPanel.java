package com.sweetcupcake.ui;

import com.sweetcupcake.model.Cupcake;
import com.sweetcupcake.service.CupcakeService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ViewCupcakesPanel extends JPanel implements DashboardFrame.RefreshablePanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel countLabel;
    private final CupcakeService service = CupcakeService.getInstance();

    public ViewCupcakesPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BG_PRIMARY);
        initUI();
        loadData();
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
        JLabel title = UITheme.titleLabel("🧁  All Cupcakes");
        title.setFont(UITheme.fontTitle(22));
        titleRow.add(title, BorderLayout.WEST);

        countLabel = new JLabel("");
        countLabel.setFont(UITheme.fontItalic(13));
        countLabel.setForeground(UITheme.TEXT_SECONDARY);
        titleRow.add(countLabel, BorderLayout.EAST);

        header.add(titleRow, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Browse our complete collection of delicious cupcakes");
        subtitle.setFont(UITheme.fontItalic(13));
        subtitle.setForeground(UITheme.TEXT_SECONDARY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(4,0,0,0));
        header.add(subtitle, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"#", "Name", "Flavor", "Category", "Price", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        UITheme.setupTable(table);

        // Column widths
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setMaxWidth(100);

        // Custom renderer for Status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(val.toString(), SwingConstants.CENTER);
                lbl.setFont(UITheme.fontBold(11));
                lbl.setOpaque(true);
                if ("Available".equals(val)) {
                    lbl.setBackground(new Color(0xE8, 0xF5, 0xE9));
                    lbl.setForeground(UITheme.SUCCESS);
                } else {
                    lbl.setBackground(new Color(0xFF, 0xEB, 0xEE));
                    lbl.setForeground(UITheme.ERROR);
                }
                return lbl;
            }
        });

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_PRIMARY);
                    c.setForeground(UITheme.TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        // Re-apply status renderer after default
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(" " + val + " ", SwingConstants.CENTER);
                lbl.setFont(UITheme.fontBold(11));
                lbl.setOpaque(true);
                if (sel) {
                    lbl.setBackground(UITheme.BG_SECONDARY);
                } else {
                    lbl.setBackground("Available".equals(val) ? new Color(0xE8, 0xF5, 0xE9) : new Color(0xFF, 0xEB, 0xEE));
                }
                lbl.setForeground("Available".equals(val) ? UITheme.SUCCESS : UITheme.ERROR);
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(UITheme.BG_PRIMARY);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        tableWrapper.add(scroll, BorderLayout.CENTER);

        add(tableWrapper, BorderLayout.CENTER);
    }

    public void loadData() {
        model.setRowCount(0);
        List<Cupcake> cupcakes = service.getAllCupcakes();
        for (int i = 0; i < cupcakes.size(); i++) {
            Cupcake c = cupcakes.get(i);
            model.addRow(new Object[]{
                i + 1, c.getName(), c.getFlavor(), c.getCategory(),
                c.getFormattedPrice(), c.isAvailable() ? "Available" : "Unavailable"
            });
        }
        countLabel.setText(cupcakes.size() + " items");
    }

    @Override public void refresh() { loadData(); }
}
