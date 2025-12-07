package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Admin;
import java.util.List;

public interface AdminService {
    Admin createAdmin(Admin admin);
    List<Admin> getAllAdmins();
    Admin getAdminById(Long id);
    Admin updateAdmin(Admin admin);
    void deleteAdmin(Long id);
}
