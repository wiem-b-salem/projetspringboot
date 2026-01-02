package org.tp1ex2.projetspring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tp1ex2.projetspring.model.Admin;
import org.tp1ex2.projetspring.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminDataInitializer.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin already exists
        Admin existingAdmin = adminRepository.findByLogin("admin");
        
        if (existingAdmin == null) {
            // Create default admin
            Admin admin = new Admin();
            admin.setLogin("admin");
            admin.setPassword("admin");
            adminRepository.save(admin);
            logger.info("Default admin created - Login: admin, Password: admin");
        } else {
            logger.info("Admin already exists in database");
        }
    }
}

