package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Favorite;
import java.util.List;

public interface FavoriteService {
    Favorite createFavorite(Favorite favorite);
    List<Favorite> getAllFavorites();
    Favorite getFavoriteById(Long id);
    Favorite updateFavorite(Favorite favorite);
    void deleteFavorite(Long id);
}
