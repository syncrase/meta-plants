package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.repository.MicroOrdreRepository;
import fr.syncrase.ecosyst.service.MicroOrdreQueryService;
import fr.syncrase.ecosyst.service.criteria.MicroOrdreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class MicroOrdreWrapper implements CronquistRank {
    private final MicroOrdre microOrdre;
    private final MicroOrdreQueryService microOrdreQueryService;
    private final MicroOrdreRepository microOrdreRepository;

    public MicroOrdreWrapper(MicroOrdre microOrdre, MicroOrdreQueryService microOrdreQueryService, MicroOrdreRepository microOrdreRepository) {
        this.microOrdre = microOrdre;
        this.microOrdreQueryService = microOrdreQueryService;
        this.microOrdreRepository = microOrdreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return microOrdre.getInfraOrdre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return microOrdre.getSuperFamilles();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (microOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && microOrdre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        MicroOrdreCriteria microOrdreCrit = new MicroOrdreCriteria();
        microOrdreCrit.setNomFr(getStringFilterEquals(microOrdre.getNomFr()));
        return microOrdreQueryService.findByCriteria(microOrdreCrit).stream().map(microOrdre1 -> new MicroOrdreWrapper(microOrdre1, microOrdreQueryService, microOrdreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return microOrdre.getId();
    }

    @Override
    public void setId(Long id) {
        microOrdre.setId(id);
    }

    @Override
    public String getNomFr() {
        return microOrdre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        microOrdre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return microOrdre;
    }

    @Override
    public void save() {
        microOrdreRepository.save(microOrdre);
    }
}
