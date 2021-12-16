package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.SuperClasseRepository;
import fr.syncrase.ecosyst.service.SuperClasseQueryService;
import fr.syncrase.ecosyst.service.criteria.SuperClasseCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SuperClasseWrapper implements CronquistRank {
    private final SuperClasse superClasse;
    private final SuperClasseQueryService superClasseQueryService;
    private final SuperClasseRepository superClasseRepository;

    public SuperClasseWrapper(SuperClasse superClasse, SuperClasseQueryService superClasseQueryService, SuperClasseRepository superClasseRepository) {
        this.superClasse = superClasse;
        this.superClasseQueryService = superClasseQueryService;
        this.superClasseRepository = superClasseRepository;
    }

    @Override
    public CronquistRank getParent() {
        return superClasse.getMicroEmbranchement();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return superClasse.getClasses();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (superClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && superClasse.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SuperClasseCriteria superClasseCrit = new SuperClasseCriteria();
        superClasseCrit.setNomFr(getStringFilterEquals(superClasse.getNomFr()));
        return superClasseQueryService.findByCriteria(superClasseCrit).stream().map(superClasse1 -> new SuperClasseWrapper(superClasse1, superClasseQueryService, superClasseRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return superClasse.getId();
    }

    @Override
    public void setId(Long id) {
        superClasse.setId(id);
    }

    @Override
    public String getNomFr() {
        return superClasse.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        superClasse.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return superClasse;
    }

    @Override
    public void save() {
        superClasseRepository.save(superClasse);
    }
}
