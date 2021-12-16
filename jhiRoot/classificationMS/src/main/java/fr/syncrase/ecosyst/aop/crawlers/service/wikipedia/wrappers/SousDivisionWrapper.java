package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.repository.SousDivisionRepository;
import fr.syncrase.ecosyst.service.SousDivisionQueryService;
import fr.syncrase.ecosyst.service.criteria.SousDivisionCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousDivisionWrapper implements CronquistRank {
    private final SousDivision sousDivision;
    private final SousDivisionQueryService sousDivisionQueryService;
    private final SousDivisionRepository sousDivisionRepository;

    public SousDivisionWrapper(SousDivision sousDivision, SousDivisionQueryService sousDivisionQueryService, SousDivisionRepository sousDivisionRepository) {
        this.sousDivision = sousDivision;
        this.sousDivisionQueryService = sousDivisionQueryService;
        this.sousDivisionRepository = sousDivisionRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousDivision.getDivision();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousDivision.getInfraEmbranchements();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousDivision.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousDivision.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousDivisionCriteria sousDivisionCrit = new SousDivisionCriteria();
        sousDivisionCrit.setNomFr(getStringFilterEquals(sousDivision.getNomFr()));
        return sousDivisionQueryService.findByCriteria(sousDivisionCrit).stream().map(sousDivision1 -> new SousDivisionWrapper(sousDivision1, sousDivisionQueryService, sousDivisionRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousDivision.getId();
    }

    @Override
    public void setId(Long id) {
        sousDivision.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousDivision.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousDivision.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousDivision;
    }

    @Override
    public void save() {
        sousDivisionRepository.save(sousDivision);
    }
}
