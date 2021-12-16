package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.repository.SousFamilleRepository;
import fr.syncrase.ecosyst.service.SousFamilleQueryService;
import fr.syncrase.ecosyst.service.criteria.SousFamilleCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousFamilleWrapper implements CronquistRank {
    private final SousFamille sousFamille;
    private final SousFamilleQueryService sousFamilleQueryService;
    private final SousFamilleRepository sousFamilleRepository;

    public SousFamilleWrapper(SousFamille sousFamille, SousFamilleQueryService sousFamilleQueryService, SousFamilleRepository sousFamilleRepository) {
        this.sousFamille = sousFamille;
        this.sousFamilleQueryService = sousFamilleQueryService;
        this.sousFamilleRepository = sousFamilleRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousFamille.getFamille();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousFamille.getTribuses();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousFamille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousFamille.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousFamilleCriteria sousFamilleCrit = new SousFamilleCriteria();
        sousFamilleCrit.setNomFr(getStringFilterEquals(sousFamille.getNomFr()));
        return sousFamilleQueryService.findByCriteria(sousFamilleCrit).stream().map(sousFamille1 -> new SousFamilleWrapper(sousFamille1, sousFamilleQueryService, sousFamilleRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousFamille.getId();
    }

    @Override
    public void setId(Long id) {
        sousFamille.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousFamille.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousFamille.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousFamille;
    }

    @Override
    public void save() {
        sousFamilleRepository.save(sousFamille);
    }
}
