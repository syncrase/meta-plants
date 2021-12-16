package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.repository.SousTribuRepository;
import fr.syncrase.ecosyst.service.SousTribuQueryService;
import fr.syncrase.ecosyst.service.criteria.SousTribuCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousTribuWrapper implements CronquistRank {
    private final SousTribu sousTribu;
    private final SousTribuQueryService sousTribuQueryService;
    private final SousTribuRepository sousTribuRepository;

    public SousTribuWrapper(SousTribu sousTribu, SousTribuQueryService sousTribuQueryService, SousTribuRepository sousTribuRepository) {
        this.sousTribu = sousTribu;
        this.sousTribuQueryService = sousTribuQueryService;
        this.sousTribuRepository = sousTribuRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousTribu.getTribu();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousTribu.getGenres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousTribu.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousTribu.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousTribuCriteria sousTribuCrit = new SousTribuCriteria();
        sousTribuCrit.setNomFr(getStringFilterEquals(sousTribu.getNomFr()));
        return sousTribuQueryService.findByCriteria(sousTribuCrit).stream().map(sousTribu -> new SousTribuWrapper(sousTribu, sousTribuQueryService, sousTribuRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousTribu.getId();
    }

    @Override
    public void setId(Long id) {
        sousTribu.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousTribu.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousTribu.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousTribu;
    }

    @Override
    public void save() {
        sousTribuRepository.save(sousTribu);
    }
}
