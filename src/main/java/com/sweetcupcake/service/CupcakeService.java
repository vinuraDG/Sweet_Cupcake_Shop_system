package com.sweetcupcake.service;

import com.sweetcupcake.dao.CupcakeDAO;
import com.sweetcupcake.dao.CupcakeDAOImpl;
import com.sweetcupcake.model.Cupcake;
import java.util.List;

public class CupcakeService {
    private static CupcakeService instance;
    private final CupcakeDAO cupcakeDAO = new CupcakeDAOImpl();

    private CupcakeService() {}

    public static CupcakeService getInstance() {
        if (instance == null) instance = new CupcakeService();
        return instance;
    }

    public boolean addCupcake(String name, String flavor, String category, double price, String description) {
        if (name == null || name.isBlank()) return false;
        if (price <= 0) return false;
        Cupcake cupcake = new Cupcake(name.trim(), flavor.trim(), category.trim(), price, description.trim());
        return cupcakeDAO.addCupcake(cupcake);
    }

    public List<Cupcake> getAllCupcakes() { return cupcakeDAO.getAllCupcakes(); }

    public List<Cupcake> getCupcakesByCategory(String category) { return cupcakeDAO.getCupcakesByCategory(category); }

    public List<Cupcake> searchCupcakes(String keyword) { return cupcakeDAO.searchCupcakes(keyword); }

    public List<String> getAllCategories() { return cupcakeDAO.getAllCategories(); }
}
