package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.repository.InfraEmbranchementRepository;
import fr.syncrase.ecosyst.service.InfraEmbranchementQueryService;
import fr.syncrase.ecosyst.service.criteria.InfraEmbranchementCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class InfraEmbranchementWrapper implements CronquistRank {
    private final InfraEmbranchement infraEmbranchement;
    private final InfraEmbranchementQueryService infraEmbranchementQueryService;
    private final InfraEmbranchementRepository infraEmbranchementRepository;

    public InfraEmbranchementWrapper(InfraEmbranchement infraEmbranchement, InfraEmbranchementQueryService infraEmbranchementQueryService, InfraEmbranchementRepository infraEmbranchementRepository) {
        this.infraEmbranchement = infraEmbranchement;
        this.infraEmbranchementQueryService = infraEmbranchementQueryService;
        this.infraEmbranchementRepository = infraEmbranchementRepository;
    }

    @Override
    public CronquistRank getParent() {
        return infraEmbranchement.getSousDivision();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return infraEmbranchement.getMicroEmbranchements();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (infraEmbranchement.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && infraEmbranchement.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        InfraEmbranchementCriteria infraEmbranchementCrit = new InfraEmbranchementCriteria();
        infraEmbranchementCrit.setNomFr(getStringFilterEquals(infraEmbranchement.getNomFr()));
        return infraEmbranchementQueryService.findByCriteria(infraEmbranchementCrit).stream().map(infraEmbranchement1 -> new InfraEmbranchementWrapper(infraEmbranchement1, infraEmbranchementQueryService, infraEmbranchementRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return infraEmbranchement.getId();
    }

    @Override
    public void setId(Long id) {
        infraEmbranchement.setId(id);
    }

    @Override
    public String getNomFr() {
        return infraEmbranchement.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        infraEmbranchement.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return infraEmbranchement;
    }

    @Override
    public void save() {
        infraEmbranchementRepository.save(infraEmbranchement);
    }
}
