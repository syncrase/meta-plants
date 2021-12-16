package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.TribuRepository;
import fr.syncrase.ecosyst.service.TribuQueryService;
import fr.syncrase.ecosyst.service.criteria.TribuCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class TribuWrapper implements CronquistRank {
    private final Tribu tribu;
    private final TribuQueryService tribuQueryService;
    private final TribuRepository tribuRepository;

    public TribuWrapper(Tribu tribu, TribuQueryService tribuQueryService, TribuRepository tribuRepository) {
        this.tribu = tribu;
        this.tribuQueryService = tribuQueryService;
        this.tribuRepository = tribuRepository;
    }

    @Override
    public CronquistRank getParent() {
        return tribu.getSousFamille();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return tribu.getSousTribuses();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (tribu.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && tribu.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        TribuCriteria tribuCrit = new TribuCriteria();
        tribuCrit.setNomFr(getStringFilterEquals(tribu.getNomFr()));
        return tribuQueryService.findByCriteria(tribuCrit).stream().map(tribu1 -> new TribuWrapper(tribu1, tribuQueryService, tribuRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return tribu.getId();
    }

    @Override
    public void setId(Long id) {
        tribu.setId(id);
    }

    @Override
    public String getNomFr() {
        return tribu.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        tribu.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return tribu;
    }

    @Override
    public void save() {
        tribuRepository.save(tribu);
    }
}
