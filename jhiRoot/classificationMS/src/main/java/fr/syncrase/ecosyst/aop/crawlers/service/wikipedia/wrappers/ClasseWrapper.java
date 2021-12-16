package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.repository.ClasseRepository;
import fr.syncrase.ecosyst.service.ClasseQueryService;
import fr.syncrase.ecosyst.service.criteria.ClasseCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class ClasseWrapper implements CronquistRank {
    private final Classe classe;
    private final ClasseQueryService classeQueryService;
    private final ClasseRepository classeRepository;

    public ClasseWrapper(Classe classe, ClasseQueryService classeQueryService, ClasseRepository classeRepository) {
        this.classe = classe;
        this.classeQueryService = classeQueryService;
        this.classeRepository = classeRepository;
    }

    @Override
    public CronquistRank getParent() {
        return classe.getSuperClasse();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return classe.getSousClasses();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (classe.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && classe.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        ClasseCriteria classeCrit = new ClasseCriteria();
        classeCrit.setNomFr(getStringFilterEquals(classe.getNomFr()));
        return classeQueryService.findByCriteria(classeCrit).stream().map(classe -> new ClasseWrapper(classe, classeQueryService, classeRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return classe.getId();
    }

    @Override
    public void setId(Long id) {
        classe.setId(id);
    }

    @Override
    public String getNomFr() {
        return classe.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        classe.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return classe;
    }

    @Override
    public void save() {
        classeRepository.save(classe);
    }
}
