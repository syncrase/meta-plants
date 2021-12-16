package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.repository.MicroEmbranchementRepository;
import fr.syncrase.ecosyst.service.MicroEmbranchementQueryService;
import fr.syncrase.ecosyst.service.criteria.MicroEmbranchementCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class MicroEmbranchementWrapper implements CronquistRank {
    private final MicroEmbranchement microEmbranchement;
    private final MicroEmbranchementQueryService microEmbranchementQueryService;
    private final MicroEmbranchementRepository microEmbranchementRepository;

    public MicroEmbranchementWrapper(MicroEmbranchement microEmbranchement, MicroEmbranchementQueryService microEmbranchementQueryService, MicroEmbranchementRepository microEmbranchementRepository) {
        this.microEmbranchement = microEmbranchement;
        this.microEmbranchementQueryService = microEmbranchementQueryService;
        this.microEmbranchementRepository = microEmbranchementRepository;
    }

    @Override
    public CronquistRank getParent() {
        return microEmbranchement.getInfraEmbranchement();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return microEmbranchement.getSuperClasses();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (microEmbranchement.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && microEmbranchement.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        MicroEmbranchementCriteria microEmbranchementCrit = new MicroEmbranchementCriteria();
        microEmbranchementCrit.setNomFr(getStringFilterEquals(microEmbranchement.getNomFr()));
        return microEmbranchementQueryService.findByCriteria(microEmbranchementCrit).stream().map(microEmbranchement1 -> new MicroEmbranchementWrapper(microEmbranchement1, microEmbranchementQueryService, microEmbranchementRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return microEmbranchement.getId();
    }

    @Override
    public void setId(Long id) {
        microEmbranchement.setId(id);
    }

    @Override
    public String getNomFr() {
        return microEmbranchement.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        microEmbranchement.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return microEmbranchement;
    }

    @Override
    public void save() {
        microEmbranchementRepository.save(microEmbranchement);
    }
}
