package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Favorite;
import java.util.List;

public interface FavoriteService {
    Favorite create(Favorite favorite);
    Favorite createFavorite(Favorite favorite);
    List<Favorite> getAll();
    List<Favorite> getAllFavorites();
    Favorite getById(Long id);
    Favorite getFavoriteById(Long id);
    Favorite update(Favorite favorite);
    Favorite updateFavorite(Favorite favorite);
    void delete(Long id);
    void deleteFavorite(Long id);
    List<Favorite> getUserFavorites(Long userId);
}
