package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.repository.SuperOrdreRepository;
import fr.syncrase.ecosyst.service.SuperOrdreQueryService;
import fr.syncrase.ecosyst.service.criteria.SuperOrdreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SuperOrdreWrapper implements CronquistRank {
    private final SuperOrdre superOrdre;
    private final SuperOrdreQueryService superOrdreQueryService;
    private final SuperOrdreRepository superOrdreRepository;

    public SuperOrdreWrapper(SuperOrdre superOrdre, SuperOrdreQueryService superOrdreQueryService, SuperOrdreRepository superOrdreRepository) {
        this.superOrdre = superOrdre;
        this.superOrdreQueryService = superOrdreQueryService;
        this.superOrdreRepository = superOrdreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return superOrdre.getInfraClasse();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superOrdre.getOrdres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (superOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && superOrdre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SuperOrdreCriteria superOrdreCrit = new SuperOrdreCriteria();
        superOrdreCrit.setNomFr(getStringFilterEquals(superOrdre.getNomFr()));
        return superOrdreQueryService.findByCriteria(superOrdreCrit).stream().map(superOrdre1 -> new SuperOrdreWrapper(superOrdre1, superOrdreQueryService, superOrdreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return superOrdre.getId();
    }

    @Override
    public void setId(Long id) {
        superOrdre.setId(id);
    }

    @Override
    public String getNomFr() {
        return superOrdre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        superOrdre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return superOrdre;
    }

    @Override
    public void save() {
        superOrdreRepository.save(superOrdre);
    }
}
