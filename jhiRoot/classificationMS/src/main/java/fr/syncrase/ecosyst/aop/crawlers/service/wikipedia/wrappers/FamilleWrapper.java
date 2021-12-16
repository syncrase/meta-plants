package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.repository.FamilleRepository;
import fr.syncrase.ecosyst.service.FamilleQueryService;
import fr.syncrase.ecosyst.service.criteria.FamilleCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class FamilleWrapper implements CronquistRank {
    private final Famille famille;
    private final FamilleQueryService familleQueryService;
    private final FamilleRepository familleRepository;

    public FamilleWrapper(Famille famille, FamilleQueryService familleQueryService, FamilleRepository familleRepository) {
        this.famille = famille;
        this.familleQueryService = familleQueryService;
        this.familleRepository = familleRepository;
    }

    @Override
    public CronquistRank getParent() {
        return famille.getSuperFamille();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return famille.getSousFamilles();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (famille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && famille.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        FamilleCriteria familleCrit = new FamilleCriteria();
        familleCrit.setNomFr(getStringFilterEquals(famille.getNomFr()));
        return familleQueryService.findByCriteria(familleCrit).stream().map(famille1 -> new FamilleWrapper(famille1, familleQueryService, familleRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return famille.getId();
    }

    @Override
    public void setId(Long id) {
        famille.setId(id);
    }

    @Override
    public String getNomFr() {
        return famille.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        famille.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return famille;
    }

    @Override
    public void save() {
        familleRepository.save(famille);
    }
}
