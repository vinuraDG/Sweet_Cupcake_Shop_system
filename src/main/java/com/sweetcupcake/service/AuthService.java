package com.sweetcupcake.service;

import com.sweetcupcake.dao.UserDAO;
import com.sweetcupcake.dao.UserDAOImpl;
import com.sweetcupcake.model.User;
import java.util.List;

public class AuthService {
    private static AuthService instance;
    private final UserDAO userDAO = new UserDAOImpl();
    private User currentUser;

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) instance = new AuthService();
        return instance;
    }

    public User login(String username, String password) {
        currentUser = userDAO.authenticate(username, password);
        return currentUser;
    }

    public void logout() { currentUser = null; }

    public User getCurrentUser() { return currentUser; }

    public boolean isLoggedIn() { return currentUser != null; }

    public boolean isManager() { return isLoggedIn() && currentUser.isManager(); }

    public boolean createCashier(String username, String password, String fullName) {
        if (!isManager()) return false;
        if (userDAO.usernameExists(username)) return false;
        User cashier = new User(username, password, fullName, User.Role.CASHIER);
        return userDAO.createUser(cashier);
    }

    public List<User> getAllUsers() { return userDAO.getAllUsers(); }
}
