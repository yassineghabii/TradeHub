package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.SupportP;
import com.example.pifinance_back.Entities.Type;

import java.util.List;

public interface SupportPService {

    SupportP addSupportP(SupportP supportP);

    List<SupportP> retrieveAllSupportP();
    void removeSupportP(int idSupportP );
    SupportP updateSupportP(SupportP supportP);
    SupportP retrieveSupportP(int idSupportP);
    List<SupportP> TrierSupportparType(Type type);
    Long countByTypeVideo();
    Long countByTypeLivre();
    Long countByTypeArticle();
}
