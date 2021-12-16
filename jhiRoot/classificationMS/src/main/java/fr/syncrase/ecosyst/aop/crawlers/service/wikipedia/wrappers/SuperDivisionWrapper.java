package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.SuperDivisionRepository;
import fr.syncrase.ecosyst.service.SuperDivisionQueryService;
import fr.syncrase.ecosyst.service.criteria.SuperDivisionCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SuperDivisionWrapper implements CronquistRank {
    private final SuperDivision superDivision;
    private final SuperDivisionQueryService superDivisionQueryService;
    private final SuperDivisionRepository superDivisionRepository;

    public SuperDivisionWrapper(SuperDivision superDivision, SuperDivisionQueryService superDivisionQueryService, SuperDivisionRepository superDivisionRepository) {
        this.superDivision = superDivision;
        this.superDivisionQueryService = superDivisionQueryService;
        this.superDivisionRepository = superDivisionRepository;
    }

    @Override
    public CronquistRank getParent() {
        return superDivision.getInfraRegne();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superDivision.getDivisions();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (superDivision.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && superDivision.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SuperDivisionCriteria superDivisionCrit = new SuperDivisionCriteria();
        superDivisionCrit.setNomFr(getStringFilterEquals(superDivision.getNomFr()));
        return superDivisionQueryService.findByCriteria(superDivisionCrit).stream().map(superDivision1 -> new SuperDivisionWrapper(superDivision1, superDivisionQueryService, superDivisionRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return superDivision.getId();
    }

    @Override
    public void setId(Long id) {
        superDivision.setId(id);
    }

    @Override
    public String getNomFr() {
        return superDivision.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        superDivision.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return superDivision;
    }

    @Override
    public void save() {
        superDivisionRepository.save(superDivision);
    }
}
