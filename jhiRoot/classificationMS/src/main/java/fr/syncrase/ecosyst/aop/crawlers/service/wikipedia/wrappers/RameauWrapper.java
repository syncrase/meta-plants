package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.repository.RameauRepository;
import fr.syncrase.ecosyst.service.RameauQueryService;
import fr.syncrase.ecosyst.service.criteria.RameauCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class RameauWrapper implements CronquistRank {
    private final Rameau rameau;
    private final RameauQueryService rameauQueryService;
    private final RameauRepository rameauRepository;

    public RameauWrapper(Rameau rameau, RameauQueryService rameauQueryService, RameauRepository rameauRepository) {
        this.rameau = rameau;
        this.rameauQueryService = rameauQueryService;
        this.rameauRepository = rameauRepository;
    }

    @Override
    public CronquistRank getParent() {
        return rameau.getSousRegne();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return rameau.getInfraRegnes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (rameau.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && rameau.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        RameauCriteria rameauCrit = new RameauCriteria();
        rameauCrit.setNomFr(getStringFilterEquals(rameau.getNomFr()));
        return rameauQueryService.findByCriteria(rameauCrit).stream().map(rameau1 -> new RameauWrapper(rameau1, rameauQueryService, rameauRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return rameau.getId();
    }

    @Override
    public void setId(Long id) {
        rameau.setId(id);
    }

    @Override
    public String getNomFr() {
        return rameau.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        rameau.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return rameau;
    }

    @Override
    public void save() {
        rameauRepository.save(rameau);
    }
}
