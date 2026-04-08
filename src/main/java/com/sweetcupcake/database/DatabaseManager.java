package com.sweetcupcake.database;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:data/sweetcupcake.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return connection;
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            seedDefaultData();
        } catch (SQLException e) {
            System.err.println("Database init error: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        String usersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                full_name TEXT NOT NULL,
                role TEXT NOT NULL CHECK(role IN ('CASHIER','MANAGER')),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String cupcakesTable = """
            CREATE TABLE IF NOT EXISTS cupcakes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                flavor TEXT NOT NULL,
                category TEXT NOT NULL,
                price REAL NOT NULL,
                description TEXT,
                is_available INTEGER DEFAULT 1,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(cupcakesTable);
        }
    }

    private void seedDefaultData() throws SQLException {
        // Seed default manager
        String checkManager = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkManager)) {
            if (rs.getInt(1) == 0) {
                String insertManager = """
                    INSERT INTO users (username, password, full_name, role)
                    VALUES ('admin', 'admin123', 'Store Manager', 'MANAGER')
                """;
                stmt.execute(insertManager);

                // Seed sample cupcakes
                String[] inserts = {
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Classic Vanilla Dream', 'Vanilla', 'Classic Flavors', 2.50, 'Soft vanilla sponge with creamy buttercream')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Dark Chocolate Bliss', 'Chocolate', 'Classic Flavors', 2.75, 'Rich dark chocolate with ganache topping')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Red Velvet Romance', 'Red Velvet', 'Classic Flavors', 3.00, 'Velvety red sponge with cream cheese frosting')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Strawberry Sunshine', 'Strawberry', 'Fruity Delights', 3.25, 'Fresh strawberry with strawberry glaze')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Lemon Zest Pop', 'Lemon', 'Fruity Delights', 3.00, 'Tangy lemon curd with lemon buttercream')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Pumpkin Spice Special', 'Pumpkin Spice', 'Seasonal Specials', 3.50, 'Autumn-inspired pumpkin with cinnamon cream')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Gluten-Free Almond Joy', 'Almond', 'Gluten-Free', 4.00, 'Almond flour base with coconut frosting')",
                    "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES ('Birthday Bash Custom', 'Vanilla', 'Custom Orders', 5.00, 'Customizable birthday cupcake with sprinkles')"
                };
                for (String sql : inserts) {
                    stmt.execute(sql);
                }
            }
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close error: " + e.getMessage());
        }
    }
}
