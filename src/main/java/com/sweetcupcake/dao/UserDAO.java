package com.sweetcupcake.dao;

import com.sweetcupcake.model.User;
import java.util.List;

public interface UserDAO {
    User authenticate(String username, String password);
    boolean createUser(User user);
    List<User> getAllUsers();
    boolean usernameExists(String username);
}
