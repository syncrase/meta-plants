package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.repository.SuperRegneRepository;
import fr.syncrase.ecosyst.service.SuperRegneQueryService;
import fr.syncrase.ecosyst.service.criteria.SuperRegneCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SuperRegneWrapper implements CronquistRank {
    private final SuperRegne superRegne;
    private final SuperRegneQueryService superRegneQueryService;
    private final SuperRegneRepository superRegneRepository;

    public SuperRegneWrapper(SuperRegne superRegne, SuperRegneQueryService superRegneQueryService, SuperRegneRepository superRegneRepository) {
        this.superRegne = superRegne;
        this.superRegneQueryService = superRegneQueryService;
        this.superRegneRepository = superRegneRepository;
    }

    @Override
    public CronquistRank getParent() {
        return null;
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superRegne.getRegnes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (superRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && superRegne.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SuperRegneCriteria superRegneCrit = new SuperRegneCriteria();
        superRegneCrit.setNomFr(getStringFilterEquals(superRegne.getNomFr()));
        return superRegneQueryService.findByCriteria(superRegneCrit).stream().map(superRegne1 -> new SuperRegneWrapper(superRegne1, superRegneQueryService, superRegneRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return superRegne.getId();
    }

    @Override
    public void setId(Long id) {
        superRegne.setId(id);
    }

    @Override
    public String getNomFr() {
        return superRegne.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        superRegne.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return superRegne;
    }

    @Override
    public void save() {
        superRegneRepository.save(superRegne);
    }
}
