package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tp1ex2.projetspring.model.Guide;
import org.tp1ex2.projetspring.repository.GuideRepository;
import org.tp1ex2.projetspring.service.GuideService;

import java.util.List;

@Service
public class GuideServiceImpl implements GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Override
    public Guide createGuide(Guide guide) {
        return guideRepository.save(guide);
    }

    @Override
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    @Override
    public Guide getGuideById(Long id) {
        return guideRepository.findById(id).orElse(null);
    }

    @Override
    public Guide updateGuide(Guide guide) {
        return guideRepository.saveAndFlush(guide);
    }

    @Override
    public void deleteGuide(Long id) {
        guideRepository.deleteById(id);
    }
}
