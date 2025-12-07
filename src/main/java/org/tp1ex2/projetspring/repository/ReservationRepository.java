package org.tp1ex2.projetspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tp1ex2.projetspring.model.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByTourId(Long tourId);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.tour.id = :tourId")
    Reservation findByUserAndTour(@Param("userId") Long userId, @Param("tourId") Long tourId);
}