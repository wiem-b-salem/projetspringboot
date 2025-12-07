package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Admin;
import java.util.List;

public interface AdminService {
    Admin create(Admin admin);
    Admin createAdmin(Admin admin);
    List<Admin> getAll();
    List<Admin> getAllAdmins();
    Admin getById(Long id);
    Admin getAdminById(Long id);
    Admin update(Admin admin);
    Admin updateAdmin(Admin admin);
    void delete(Long id);
    void deleteAdmin(Long id);
    Admin findByLogin(String login);
}
