package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.repository.SousEspeceRepository;
import fr.syncrase.ecosyst.service.SousEspeceQueryService;
import fr.syncrase.ecosyst.service.criteria.SousEspeceCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousEspeceWrapper implements CronquistRank {
    private final SousEspece sousEspece;
    private final SousEspeceQueryService sousEspeceQueryService;
    private final SousEspeceRepository sousEspeceRepository;

    public SousEspeceWrapper(SousEspece sousEspece, SousEspeceQueryService sousEspeceQueryService, SousEspeceRepository sousEspeceRepository) {
        this.sousEspece = sousEspece;
        this.sousEspeceQueryService = sousEspeceQueryService;
        this.sousEspeceRepository = sousEspeceRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousEspece.getEspece();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousEspece.getVarietes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousEspece.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousEspece.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousEspeceCriteria sousEspeceCrit = new SousEspeceCriteria();
        sousEspeceCrit.setNomFr(getStringFilterEquals(sousEspece.getNomFr()));
        return sousEspeceQueryService.findByCriteria(sousEspeceCrit).stream().map(sousEspece1 -> new SousEspeceWrapper(sousEspece1, sousEspeceQueryService, sousEspeceRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousEspece.getId();
    }

    @Override
    public void setId(Long id) {
        sousEspece.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousEspece.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousEspece.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousEspece;
    }

    @Override
    public void save() {
        sousEspeceRepository.save(sousEspece);
    }
}
