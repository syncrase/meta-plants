package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Division;
import fr.syncrase.ecosyst.repository.DivisionRepository;
import fr.syncrase.ecosyst.service.DivisionQueryService;
import fr.syncrase.ecosyst.service.criteria.DivisionCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class DivisionWrapper implements CronquistRank {
    private final Division division;
    private final DivisionQueryService divisionQueryService;
    private final DivisionRepository divisionRepository;

    public DivisionWrapper(Division division, DivisionQueryService divisionQueryService, DivisionRepository divisionRepository) {
        this.division = division;
        this.divisionQueryService = divisionQueryService;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public CronquistRank getParent() {
        return division.getSuperDivision();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return division.getSousDivisions();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (division.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && division.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        DivisionCriteria divisionCrit = new DivisionCriteria();
        divisionCrit.setNomFr(getStringFilterEquals(division.getNomFr()));
        return divisionQueryService.findByCriteria(divisionCrit).stream().map(division -> new DivisionWrapper(division, divisionQueryService, divisionRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return division.getId();
    }

    @Override
    public void setId(Long id) {
        division.setId(id);
    }

    @Override
    public String getNomFr() {
        return division.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        division.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return division;
    }

    @Override
    public void save() {
        divisionRepository.save(division);
    }
}
