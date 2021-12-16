package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.repository.SousClasseRepository;
import fr.syncrase.ecosyst.service.SousClasseQueryService;
import fr.syncrase.ecosyst.service.criteria.SousClasseCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousClasseWrapper implements CronquistRank {
    private final SousClasse sousClasse;
    private final SousClasseQueryService sousClasseQueryService;
    private final SousClasseRepository sousClasseRepository;

    public SousClasseWrapper(SousClasse sousClasse, SousClasseQueryService sousClasseQueryService, SousClasseRepository sousClasseRepository) {
        this.sousClasse = sousClasse;
        this.sousClasseQueryService = sousClasseQueryService;
        this.sousClasseRepository = sousClasseRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousClasse.getClasse();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousClasse.getInfraClasses();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousClasse.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousClasseCriteria sousClasseCrit = new SousClasseCriteria();
        sousClasseCrit.setNomFr(getStringFilterEquals(sousClasse.getNomFr()));
        return sousClasseQueryService.findByCriteria(sousClasseCrit).stream().map(sousClasse1 -> new SousClasseWrapper(sousClasse1, sousClasseQueryService, sousClasseRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousClasse.getId();
    }

    @Override
    public void setId(Long id) {
        sousClasse.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousClasse.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousClasse.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousClasse;
    }

    @Override
    public void save() {
        sousClasseRepository.save(sousClasse);
    }
}
