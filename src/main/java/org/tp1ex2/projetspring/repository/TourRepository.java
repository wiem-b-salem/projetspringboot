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

    @Query("SELECT t FROM Tour t WHERE t.price >= :minPrice AND t.price <= :maxPrice ORDER BY t.price ASC")
    List<Tour> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT DISTINCT t FROM Tour t WHERE t.date >= :startDate ORDER BY t.date ASC")
    List<Tour> findUpcomingTours(@Param("startDate") LocalDate startDate);

    @Query("SELECT t FROM Tour t WHERE t.name LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Tour> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT t FROM Tour t WHERE t.price >= :minPrice AND t.price <= :maxPrice AND t.date >= :startDate ORDER BY t.date ASC")
    List<Tour> findByPriceAndDate(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("startDate") LocalDate startDate);

    @Query("SELECT DISTINCT t FROM Tour t WHERE (:keyword IS NULL OR t.name LIKE %:keyword% OR t.description LIKE %:keyword%) " +
           "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR t.price <= :maxPrice) " +
           "AND (:upcoming = false OR t.date >= :startDate) " +
           "ORDER BY t.date ASC")
    List<Tour> filterTours(@Param("keyword") String keyword, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("upcoming") Boolean upcoming, @Param("startDate") LocalDate startDate);
}