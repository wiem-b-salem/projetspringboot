package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByLogin(String login);

    @Query("SELECT a FROM Admin a WHERE a.login = :login")
    Admin chercherParLogin(@Param("login") String login);
}