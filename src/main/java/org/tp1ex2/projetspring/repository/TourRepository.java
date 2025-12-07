package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Tour;

import java.time.LocalDate;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByGuideId(Long guideId);
    List<Tour> findByDate(LocalDate date);

    @Query("SELECT t FROM Tour t WHERE t.price <= :maxPrice")
    List<Tour> findByMaxPrice(@Param("maxPrice") Double maxPrice);
}