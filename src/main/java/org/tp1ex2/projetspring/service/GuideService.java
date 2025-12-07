package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Guide;
import java.util.List;

public interface GuideService {
    Guide createGuide(Guide guide);
    List<Guide> getAllGuides();
    Guide getGuideById(Long id);
    Guide updateGuide(Guide guide);
    void deleteGuide(Long id);
}
