package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByCategory(String category);
    List<Place> findByCity(String city);
    List<Place> findByNameContaining(String name);

    @Query("SELECT p FROM Place p WHERE p.category = :category AND p.city = :city")
    List<Place> findByCategoryAndCity(@Param("category") String category, @Param("city") String city);

    @Query("SELECT p FROM Place p WHERE p.category = :category")
    List<Place> findByType(@Param("category") String category);

    @Query("SELECT p FROM Place p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Place> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT p.city FROM Place p ORDER BY p.city ASC")
    List<String> findAllCities();

    @Query("SELECT DISTINCT p.category FROM Place p ORDER BY p.category ASC")
    List<String> findAllCategories();
}