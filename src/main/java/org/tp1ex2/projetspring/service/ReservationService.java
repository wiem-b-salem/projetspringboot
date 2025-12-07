package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(Long id);
}
