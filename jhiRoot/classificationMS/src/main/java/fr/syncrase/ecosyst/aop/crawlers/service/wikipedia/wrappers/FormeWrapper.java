package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.repository.FormeRepository;
import fr.syncrase.ecosyst.service.FormeQueryService;
import fr.syncrase.ecosyst.service.criteria.FormeCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class FormeWrapper implements CronquistRank {
    private final Forme forme;
    private final FormeQueryService formeQueryService;
    private final FormeRepository formeRepository;

    public FormeWrapper(Forme Forme, FormeQueryService formeQueryService, FormeRepository formeRepository) {
        this.forme = Forme;
        this.formeQueryService = formeQueryService;
        this.formeRepository = formeRepository;
    }

    @Override
    public CronquistRank getParent() {
        return forme.getSousVariete();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return forme.getSousFormes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (forme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && forme.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        FormeCriteria formeCrit = new FormeCriteria();
        formeCrit.setNomFr(getStringFilterEquals(forme.getNomFr()));
        return formeQueryService.findByCriteria(formeCrit).stream().map(forme1 -> new FormeWrapper(forme1, formeQueryService, formeRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return forme.getId();
    }

    @Override
    public void setId(Long id) {
        forme.setId(id);
    }

    @Override
    public String getNomFr() {
        return forme.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        forme.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return forme;
    }

    @Override
    public void save() {
        formeRepository.save(forme);
    }
}
