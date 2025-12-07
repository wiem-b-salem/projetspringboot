package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tp1ex2.projetspring.model.Admin;
import org.tp1ex2.projetspring.repository.AdminRepository;
import org.tp1ex2.projetspring.service.AdminService;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return adminRepository.saveAndFlush(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Admin create(Admin admin) {
        return createAdmin(admin);
    }

    @Override
    public List<Admin> getAll() {
        return getAllAdmins();
    }

    @Override
    public Admin getById(Long id) {
        return getAdminById(id);
    }

    @Override
    public Admin update(Admin admin) {
        return updateAdmin(admin);
    }

    @Override
    public void delete(Long id) {
        deleteAdmin(id);
    }

    @Override
    public Admin findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }
}
