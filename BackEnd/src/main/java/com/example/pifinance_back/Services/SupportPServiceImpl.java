package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.SupportP;
import com.example.pifinance_back.Entities.Type;
import com.example.pifinance_back.Repositories.SupportPRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SupportPServiceImpl implements SupportPService{
    private SupportPRepository supportPRepository;
    @Override
    public SupportP addSupportP(SupportP supportP) {
        return supportPRepository.save(supportP);
    }


    @Override
    public List<SupportP> retrieveAllSupportP() {
        return supportPRepository.findAll();
    }

    @Override
    public void removeSupportP(int idSupportP) {
        supportPRepository.deleteById(idSupportP);
    }

    @Override
    public SupportP updateSupportP(SupportP supportP) {

        return supportPRepository.save(supportP);
    }

    @Override
    public SupportP retrieveSupportP(int idSupportP) {
        return supportPRepository.findById(idSupportP).orElse(null);
    }

    @Override
    public List<SupportP> TrierSupportparType(Type type) {
        return supportPRepository.findByType(type);
    }

    @Override
    public Long countByTypeVideo() {
        return supportPRepository.countByTypeVideo();
    }

    @Override
    public Long countByTypeLivre() {
        return supportPRepository.countByTypeLivre();
    }

    @Override
    public Long countByTypeArticle() {
        return supportPRepository.countByTypeArticle();
    }

}
