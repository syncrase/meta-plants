package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.repository.SousFormeRepository;
import fr.syncrase.ecosyst.service.SousFormeQueryService;
import fr.syncrase.ecosyst.service.criteria.SousFormeCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;


public class SousFormeWrapper implements CronquistRank {

    private final SousFormeQueryService sousFormeQueryService;
    private final SousFormeRepository sousFormeRepository;
    private final SousForme sousForme;

    public SousFormeWrapper(SousForme sousForme, SousFormeQueryService sousFormeQueryService, SousFormeRepository sousFormeRepository) {

        this.sousForme = sousForme;
        this.sousFormeQueryService = sousFormeQueryService;
        this.sousFormeRepository = sousFormeRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousForme.getForme();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return null;
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousForme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousForme.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousFormeCriteria sousFormeCrit = new SousFormeCriteria();
        sousFormeCrit.setNomFr(getStringFilterEquals(sousForme.getNomFr()));
        return sousFormeQueryService.findByCriteria(sousFormeCrit).stream().map(sousForme1 -> new SousFormeWrapper(sousForme1, sousFormeQueryService, sousFormeRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousForme.getId();
    }

    @Override
    public void setId(Long id) {
        sousForme.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousForme.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousForme.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousForme;
    }

    @Override
    public void save() {
        sousFormeRepository.save(sousForme);
    }
}
