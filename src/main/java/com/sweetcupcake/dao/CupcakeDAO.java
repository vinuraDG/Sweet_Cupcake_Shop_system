package com.sweetcupcake.dao;

import com.sweetcupcake.model.Cupcake;
import java.util.List;

public interface CupcakeDAO {
    boolean addCupcake(Cupcake cupcake);
    List<Cupcake> getAllCupcakes();
    List<Cupcake> getCupcakesByCategory(String category);
    List<Cupcake> searchCupcakes(String keyword);
    List<String> getAllCategories();
}
