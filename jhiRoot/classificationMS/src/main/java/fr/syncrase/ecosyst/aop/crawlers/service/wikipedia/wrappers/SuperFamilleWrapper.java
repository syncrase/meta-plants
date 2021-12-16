package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.SuperFamilleRepository;
import fr.syncrase.ecosyst.service.SuperFamilleQueryService;
import fr.syncrase.ecosyst.service.criteria.SuperFamilleCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SuperFamilleWrapper implements CronquistRank {
    private final SuperFamille superFamille;
    private final SuperFamilleQueryService superFamilleQueryService;
    private final SuperFamilleRepository superFamilleRepository;

    public SuperFamilleWrapper(SuperFamille superFamille, SuperFamilleQueryService superFamilleQueryService, SuperFamilleRepository sousFamilleRepository) {
        this.superFamille = superFamille;
        this.superFamilleQueryService = superFamilleQueryService;
        this.superFamilleRepository = sousFamilleRepository;
    }

    @Override
    public CronquistRank getParent() {
        return superFamille.getMicroOrdre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superFamille.getFamilles();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (superFamille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && superFamille.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SuperFamilleCriteria superFamilleCrit = new SuperFamilleCriteria();
        superFamilleCrit.setNomFr(getStringFilterEquals(superFamille.getNomFr()));
        return superFamilleQueryService.findByCriteria(superFamilleCrit).stream().map(superFamille1 -> new SuperFamilleWrapper(superFamille1, superFamilleQueryService, superFamilleRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return superFamille.getId();
    }

    @Override
    public void setId(Long id) {
        superFamille.setId(id);
    }

    @Override
    public String getNomFr() {
        return superFamille.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        superFamille.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return superFamille;
    }

    @Override
    public void save() {
        superFamilleRepository.save(superFamille);
    }
}
