package com.sweetcupcake.dao;

import com.sweetcupcake.database.DatabaseManager;
import com.sweetcupcake.model.Cupcake;
import java.sql.*;
import java.util.*;

public class CupcakeDAOImpl implements CupcakeDAO {
    private final DatabaseManager db = DatabaseManager.getInstance();

    @Override
    public boolean addCupcake(Cupcake cupcake) {
        String sql = "INSERT INTO cupcakes (name, flavor, category, price, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cupcake.getName());
            stmt.setString(2, cupcake.getFlavor());
            stmt.setString(3, cupcake.getCategory());
            stmt.setDouble(4, cupcake.getPrice());
            stmt.setString(5, cupcake.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add cupcake error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cupcake> getAllCupcakes() {
        List<Cupcake> list = new ArrayList<>();
        String sql = "SELECT * FROM cupcakes ORDER BY category, name";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapCupcake(rs));
        } catch (SQLException e) {
            System.err.println("Get cupcakes error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Cupcake> getCupcakesByCategory(String category) {
        List<Cupcake> list = new ArrayList<>();
        String sql = "SELECT * FROM cupcakes WHERE category = ? ORDER BY name";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapCupcake(rs));
        } catch (SQLException e) {
            System.err.println("Get by category error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Cupcake> searchCupcakes(String keyword) {
        List<Cupcake> list = new ArrayList<>();
        String sql = "SELECT * FROM cupcakes WHERE name LIKE ? OR flavor LIKE ? OR category LIKE ? OR description LIKE ?";
        String pattern = "%" + keyword + "%";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) stmt.setString(i, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapCupcake(rs));
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM cupcakes ORDER BY category";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) categories.add(rs.getString("category"));
        } catch (SQLException e) {
            System.err.println("Get categories error: " + e.getMessage());
        }
        return categories;
    }

    private Cupcake mapCupcake(ResultSet rs) throws SQLException {
        Cupcake c = new Cupcake();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setFlavor(rs.getString("flavor"));
        c.setCategory(rs.getString("category"));
        c.setPrice(rs.getDouble("price"));
        c.setDescription(rs.getString("description"));
        c.setAvailable(rs.getInt("is_available") == 1);
        c.setCreatedAt(rs.getString("created_at"));
        return c;
    }
}
