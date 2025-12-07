package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tp1ex2.projetspring.model.Reservation;
import org.tp1ex2.projetspring.repository.ReservationRepository;
import org.tp1ex2.projetspring.service.ReservationService;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation create(Reservation reservation) {
        return createReservation(reservation);
    }

    @Override
    public List<Reservation> getAll() {
        return getAllReservations();
    }

    @Override
    public Reservation getById(Long id) {
        return getReservationById(id);
    }

    @Override
    public Reservation update(Reservation reservation) {
        return updateReservation(reservation);
    }

    @Override
    public void delete(Long id) {
        deleteReservation(id);
    }
}
