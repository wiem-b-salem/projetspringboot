package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Favorite;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    List<Favorite> findByPlaceId(Long placeId);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.place.id = :placeId")
    Favorite findByUserAndPlace(@Param("userId") Long userId, @Param("placeId") Long placeId);
}