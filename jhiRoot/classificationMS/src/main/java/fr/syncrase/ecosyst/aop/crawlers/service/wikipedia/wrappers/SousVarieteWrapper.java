package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.repository.SousVarieteRepository;
import fr.syncrase.ecosyst.service.SousVarieteQueryService;
import fr.syncrase.ecosyst.service.criteria.SousVarieteCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousVarieteWrapper implements CronquistRank {
    private final SousVariete sousVariete;
    private final SousVarieteQueryService sousVarieteQueryService;
    private final SousVarieteRepository sousVarieteRepository;

    public SousVarieteWrapper(SousVariete sousVariete, SousVarieteQueryService sousVarieteQueryService, SousVarieteRepository sousVarieteRepository) {
        this.sousVariete = sousVariete;
        this.sousVarieteQueryService = sousVarieteQueryService;
        this.sousVarieteRepository = sousVarieteRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousVariete.getVariete();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousVariete.getFormes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousVariete.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousVariete.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousVarieteCriteria sousVarieteCrit = new SousVarieteCriteria();
        sousVarieteCrit.setNomFr(getStringFilterEquals(sousVariete.getNomFr()));
        return sousVarieteQueryService.findByCriteria(sousVarieteCrit).stream().map(sousVariete1 -> new SousVarieteWrapper(sousVariete1, sousVarieteQueryService, sousVarieteRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousVariete.getId();
    }

    @Override
    public void setId(Long id) {
        sousVariete.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousVariete.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousVariete.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousVariete;
    }

    @Override
    public void save() {
        sousVarieteRepository.save(sousVariete);
    }
}
