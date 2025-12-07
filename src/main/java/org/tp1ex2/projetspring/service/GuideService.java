package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Guide;
import java.util.List;

public interface GuideService {
    Guide create(Guide guide);
    Guide createGuide(Guide guide);
    List<Guide> getAll();
    List<Guide> getAllGuides();
    Guide getById(Long id);
    Guide getGuideById(Long id);
    Guide update(Guide guide);
    Guide updateGuide(Guide guide);
    void delete(Long id);
    void deleteGuide(Long id);
}
