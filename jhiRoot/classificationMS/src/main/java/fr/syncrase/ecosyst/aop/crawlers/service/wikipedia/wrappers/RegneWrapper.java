package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.repository.RegneRepository;
import fr.syncrase.ecosyst.service.RegneQueryService;
import fr.syncrase.ecosyst.service.criteria.RegneCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class RegneWrapper implements CronquistRank {
    private final Regne regne;
    private final RegneQueryService regneQueryService;
    private final RegneRepository regneRepository;

    public RegneWrapper(Regne regne, RegneQueryService regneQueryService, RegneRepository regneRepository) {
        this.regne = regne;
        this.regneQueryService = regneQueryService;
        this.regneRepository = regneRepository;
    }

    @Override
    public CronquistRank getParent() {
        return regne.getSuperRegne();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return regne.getSousRegnes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (regne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && regne.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        RegneCriteria regneCrit = new RegneCriteria();
        regneCrit.setNomFr(getStringFilterEquals(regne.getNomFr()));
        return regneQueryService.findByCriteria(regneCrit).stream().map(regne1 -> new RegneWrapper(regne1, regneQueryService, regneRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return regne.getId();
    }

    @Override
    public void setId(Long id) {
        regne.setId(id);
    }

    @Override
    public String getNomFr() {
        return regne.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        regne.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return regne;
    }

    @Override
    public void save() {
        regneRepository.save(regne);
    }
}
