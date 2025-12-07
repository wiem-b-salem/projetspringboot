package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.User;
import java.util.List;

public interface UserService {
    User create(User user);
    User createUser(User user);
    List<User> getAll();
    List<User> getAllUsers();
    User getById(Long id);
    User getUserById(Long id);
    User update(User user);
    User updateUser(User user);
    void delete(Long id);
    void deleteUser(Long id);
    User findByEmail(String email);
}
