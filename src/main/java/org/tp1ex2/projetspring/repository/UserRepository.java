package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	List<User> findByFirstNameContaining(String firstName);
	List<User> findByFirstNameAndEmail(String firstName, String email);

	@Query("SELECT u FROM User u WHERE u.email = :email")
	User chercherParEmail(@Param("email") String email);

	@Query("SELECT u FROM User u WHERE u.firstName LIKE %:firstName")
	List<User> findByFirstName(@Param("firstName") String firstName);

	@Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
	User chercherParEmailSQL(@Param("email") String email);
}
