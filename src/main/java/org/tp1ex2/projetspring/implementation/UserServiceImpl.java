package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.repository.UserRepository;
import org.tp1ex2.projetspring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        try {
            logger.info("Creating user: {} {}", user.getFirstName(), user.getLastName());
            User savedUser = userRepository.save(user);
            logger.info("User created with ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error creating user", e);
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User create(User user) {
        return createUser(user);
    }

    @Override
    public List<User> getAll() {
        return getAllUsers();
    }

    @Override
    public User getById(Long id) {
        return getUserById(id);
    }

    @Override
    public User update(User user) {
        return updateUser(user);
    }

    @Override
    public void delete(Long id) {
        deleteUser(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
