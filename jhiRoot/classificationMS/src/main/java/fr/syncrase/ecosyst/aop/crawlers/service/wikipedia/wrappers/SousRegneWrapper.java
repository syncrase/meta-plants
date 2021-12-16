package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.repository.SousRegneRepository;
import fr.syncrase.ecosyst.service.SousRegneQueryService;
import fr.syncrase.ecosyst.service.criteria.SousRegneCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousRegneWrapper implements CronquistRank {
    private final SousRegne sousRegne;
    private final SousRegneQueryService sousRegneQueryService;
    private final SousRegneRepository sousRegneRepository;

    public SousRegneWrapper(SousRegne sousRegne, SousRegneQueryService sousRegneQueryService, SousRegneRepository sousRegneRepository) {
        this.sousRegne = sousRegne;
        this.sousRegneQueryService = sousRegneQueryService;
        this.sousRegneRepository = sousRegneRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousRegne.getRegne();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousRegne.getRameaus();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousRegne.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousRegneCriteria sousRegneCrit = new SousRegneCriteria();
        sousRegneCrit.setNomFr(getStringFilterEquals(sousRegne.getNomFr()));
        return sousRegneQueryService.findByCriteria(sousRegneCrit).stream().map(sousRegne1 -> new SousRegneWrapper(sousRegne1, sousRegneQueryService, sousRegneRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousRegne.getId();
    }

    @Override
    public void setId(Long id) {
        sousRegne.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousRegne.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousRegne.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousRegne;
    }

    @Override
    public void save() {
        sousRegneRepository.save(sousRegne);
    }
}
