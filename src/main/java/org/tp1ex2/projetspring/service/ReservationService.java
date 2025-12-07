package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation create(Reservation reservation);
    Reservation createReservation(Reservation reservation);
    List<Reservation> getAll();
    List<Reservation> getAllReservations();
    Reservation getById(Long id);
    Reservation getReservationById(Long id);
    Reservation update(Reservation reservation);
    Reservation updateReservation(Reservation reservation);
    void delete(Long id);
    void deleteReservation(Long id);
}
