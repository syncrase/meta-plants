package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.repository.InfraRegneRepository;
import fr.syncrase.ecosyst.service.InfraRegneQueryService;
import fr.syncrase.ecosyst.service.criteria.InfraRegneCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class InfraRegneWrapper implements CronquistRank {
    private final InfraRegne infraRegne;
    private final InfraRegneQueryService infraRegneQueryService;
    private final InfraRegneRepository infraRegneRepository;

    public InfraRegneWrapper(InfraRegne infraRegne, InfraRegneQueryService infraRegneQueryService, InfraRegneRepository infraRegneRepository) {
        this.infraRegne = infraRegne;
        this.infraRegneQueryService = infraRegneQueryService;
        this.infraRegneRepository = infraRegneRepository;
    }

    @Override
    public CronquistRank getParent() {
        return infraRegne.getRameau();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return infraRegne.getSuperDivisions();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (infraRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && infraRegne.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        InfraRegneCriteria infraRegneCrit = new InfraRegneCriteria();
        infraRegneCrit.setNomFr(getStringFilterEquals(infraRegne.getNomFr()));
        return infraRegneQueryService.findByCriteria(infraRegneCrit).stream().map(infraRegne1 -> new InfraRegneWrapper(infraRegne1, infraRegneQueryService, infraRegneRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return infraRegne.getId();
    }

    @Override
    public void setId(Long id) {
        infraRegne.setId(id);
    }

    @Override
    public String getNomFr() {
        return infraRegne.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        infraRegne.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return infraRegne;
    }

    @Override
    public void save() {
        infraRegneRepository.save(infraRegne);
    }
}
