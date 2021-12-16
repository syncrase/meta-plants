package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.SousSectionRepository;
import fr.syncrase.ecosyst.service.SousSectionQueryService;
import fr.syncrase.ecosyst.service.criteria.SousSectionCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousSectionWrapper implements CronquistRank {
    private final SousSection sousSection;
    private final SousSectionQueryService sousSectionQueryService;
    private final SousSectionRepository sousSectionRepository;

    public SousSectionWrapper(SousSection sousSection, SousSectionQueryService sousSectionQueryService, SousSectionRepository sousSectionRepository) {
        this.sousSection = sousSection;
        this.sousSectionQueryService = sousSectionQueryService;
        this.sousSectionRepository = sousSectionRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousSection.getSection();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousSection.getEspeces();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousSection.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousSection.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousSectionCriteria sousSectionCrit = new SousSectionCriteria();
        sousSectionCrit.setNomFr(getStringFilterEquals(sousSection.getNomFr()));
        return sousSectionQueryService.findByCriteria(sousSectionCrit).stream().map(sousSection1 -> new SousSectionWrapper(sousSection1, sousSectionQueryService, sousSectionRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousSection.getId();
    }

    @Override
    public void setId(Long id) {
        sousSection.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousSection.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousSection.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousSection;
    }

    @Override
    public void save() {
        sousSectionRepository.save(sousSection);
    }
}
