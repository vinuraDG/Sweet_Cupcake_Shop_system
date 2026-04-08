package com.sweetcupcake.ui;

import com.sweetcupcake.model.Cupcake;
import com.sweetcupcake.service.CupcakeService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SearchCupcakesPanel extends JPanel implements DashboardFrame.RefreshablePanel {
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JTable table;
    private DefaultTableModel model;
    private JLabel resultLabel;
    private final CupcakeService service = CupcakeService.getInstance();

    public SearchCupcakesPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_PRIMARY);
        initUI();
        loadAll();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xFF, 0xE0, 0xD0)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        JLabel title = UITheme.titleLabel("🔍  Search Cupcakes");
        header.add(title, BorderLayout.WEST);
        JLabel sub = new JLabel("Search by name, flavor, or filter by category");
        sub.setFont(UITheme.fontItalic(13));
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setBorder(BorderFactory.createEmptyBorder(4,0,0,0));
        header.add(sub, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Search controls
        JPanel controlsArea = new JPanel(new BorderLayout());
        controlsArea.setBackground(UITheme.BG_PRIMARY);
        controlsArea.setBorder(BorderFactory.createEmptyBorder(20, 28, 12, 28));

        JPanel controlsCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        controlsCard.setBackground(UITheme.BG_CARD);
        controlsCard.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(0xFF, 0xD0, 0xC0)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        JLabel searchLbl = UITheme.sectionLabel("Search:");
        searchField = UITheme.styledTextField(20);
        searchField.setPreferredSize(new Dimension(240, 36));
        searchField.setToolTipText("Search by name, flavor, description...");

        JLabel catLbl = UITheme.sectionLabel("Category:");
        categoryFilter = new JComboBox<>();
        categoryFilter.setFont(UITheme.fontRegular(13));
        categoryFilter.setPreferredSize(new Dimension(200, 36));
        loadCategories();

        JButton searchBtn = UITheme.primaryButton("🔍  Search");
        searchBtn.setPreferredSize(new Dimension(120, 36));
        searchBtn.addActionListener(e -> doSearch());

        JButton resetBtn = UITheme.secondaryButton("Reset");
        resetBtn.setPreferredSize(new Dimension(90, 36));
        resetBtn.addActionListener(e -> { searchField.setText(""); categoryFilter.setSelectedIndex(0); loadAll(); });

        searchField.addActionListener(e -> doSearch());

        controlsCard.add(searchLbl);
        controlsCard.add(searchField);
        controlsCard.add(catLbl);
        controlsCard.add(categoryFilter);
        controlsCard.add(searchBtn);
        controlsCard.add(resetBtn);

        resultLabel = new JLabel("");
        resultLabel.setFont(UITheme.fontItalic(12));
        resultLabel.setForeground(UITheme.TEXT_SECONDARY);

        controlsArea.add(controlsCard, BorderLayout.NORTH);
        controlsArea.add(resultLabel, BorderLayout.SOUTH);
        add(controlsArea, BorderLayout.NORTH);

        // Table
        String[] cols = {"#", "Name", "Flavor", "Category", "Price", "Description"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UITheme.setupTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(45);
        table.getColumnModel().getColumn(4).setMaxWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(260);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) c.setBackground(row%2==0 ? UITheme.BG_CARD : UITheme.BG_PRIMARY);
                if (!sel) c.setForeground(UITheme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(UITheme.BG_PRIMARY);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(0, 28, 24, 28));
        tableWrapper.add(scroll);
        add(tableWrapper, BorderLayout.CENTER);
    }

    private void loadCategories() {
        categoryFilter.removeAllItems();
        categoryFilter.addItem("— All Categories —");
        service.getAllCategories().forEach(categoryFilter::addItem);
    }

    private void doSearch() {
        String keyword = searchField.getText().trim();
        String selectedCat = (String) categoryFilter.getSelectedItem();
        boolean catSelected = selectedCat != null && !selectedCat.startsWith("—");

        List<Cupcake> results;
        if (catSelected && !keyword.isEmpty()) {
            results = service.getCupcakesByCategory(selectedCat);
            results = results.stream().filter(c ->
                c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                c.getFlavor().toLowerCase().contains(keyword.toLowerCase())
            ).toList();
        } else if (catSelected) {
            results = service.getCupcakesByCategory(selectedCat);
        } else if (!keyword.isEmpty()) {
            results = service.searchCupcakes(keyword);
        } else {
            loadAll(); return;
        }
        showResults(results);
        resultLabel.setText("Found " + results.size() + " result(s)");
    }

    private void loadAll() {
        showResults(service.getAllCupcakes());
        resultLabel.setText("Showing all cupcakes");
    }

    private void showResults(List<Cupcake> list) {
        model.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Cupcake c = list.get(i);
            model.addRow(new Object[]{
                i+1, c.getName(), c.getFlavor(), c.getCategory(), c.getFormattedPrice(),
                c.getDescription() != null ? c.getDescription() : ""
            });
        }
    }

    @Override public void refresh() { loadCategories(); loadAll(); }
}
