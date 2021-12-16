package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.repository.InfraOrdreRepository;
import fr.syncrase.ecosyst.service.InfraOrdreQueryService;
import fr.syncrase.ecosyst.service.criteria.InfraOrdreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;


public class InfraOrdreWrapper implements CronquistRank {
    private final InfraOrdre infraOrdre;
    private final InfraOrdreQueryService infraOrdreQueryService;
    private final InfraOrdreRepository infraOrdreRepository;

    public InfraOrdreWrapper(InfraOrdre infraOrdre, InfraOrdreQueryService infraOrdreQueryService, InfraOrdreRepository infraOrdreRepository) {
        this.infraOrdre = infraOrdre;
        this.infraOrdreQueryService = infraOrdreQueryService;
        this.infraOrdreRepository = infraOrdreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return infraOrdre.getSousOrdre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return infraOrdre.getMicroOrdres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (infraOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && infraOrdre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        InfraOrdreCriteria infraOrdreCrit = new InfraOrdreCriteria();
        infraOrdreCrit.setNomFr(getStringFilterEquals(infraOrdre.getNomFr()));
        return infraOrdreQueryService.findByCriteria(infraOrdreCrit).stream().map(infraOrdre1 -> new InfraOrdreWrapper(infraOrdre1, infraOrdreQueryService, infraOrdreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return infraOrdre.getId();
    }

    @Override
    public void setId(Long id) {
        infraOrdre.setId(id);
    }

    @Override
    public String getNomFr() {
        return infraOrdre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        infraOrdre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return infraOrdre;
    }

    @Override
    public void save() {
        infraOrdreRepository.save(infraOrdre);
    }
}
