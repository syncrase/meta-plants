package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.repository.SousOrdreRepository;
import fr.syncrase.ecosyst.service.SousOrdreQueryService;
import fr.syncrase.ecosyst.service.criteria.SousOrdreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousOrdreWrapper implements CronquistRank {
    private final SousOrdre sousOrdre;
    private final SousOrdreQueryService sousOrdreQueryService;
    private final SousOrdreRepository sousOrdreRepository;

    public SousOrdreWrapper(SousOrdre sousOrdre, SousOrdreQueryService sousOrdreQueryService, SousOrdreRepository sousOrdreRepository) {
        this.sousOrdre = sousOrdre;
        this.sousOrdreQueryService = sousOrdreQueryService;
        this.sousOrdreRepository = sousOrdreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousOrdre.getOrdre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousOrdre.getInfraOrdres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousOrdre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousOrdreCriteria sousOrdreCrit = new SousOrdreCriteria();
        sousOrdreCrit.setNomFr(getStringFilterEquals(sousOrdre.getNomFr()));
        return sousOrdreQueryService.findByCriteria(sousOrdreCrit).stream().map(sousOrdre1 -> new SousOrdreWrapper(sousOrdre1, sousOrdreQueryService, sousOrdreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousOrdre.getId();
    }

    @Override
    public void setId(Long id) {
        sousOrdre.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousOrdre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousOrdre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousOrdre;
    }

    @Override
    public void save() {
        sousOrdreRepository.save(sousOrdre);
    }
}
