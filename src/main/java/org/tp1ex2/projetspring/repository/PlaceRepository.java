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
}