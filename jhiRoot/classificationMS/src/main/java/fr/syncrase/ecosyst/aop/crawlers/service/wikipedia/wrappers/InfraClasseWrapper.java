package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.repository.InfraClasseRepository;
import fr.syncrase.ecosyst.service.InfraClasseQueryService;
import fr.syncrase.ecosyst.service.criteria.InfraClasseCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class InfraClasseWrapper implements CronquistRank {
    private final InfraClasse infraClasse;
    private final InfraClasseQueryService infraClasseQueryService;
    private final InfraClasseRepository infraClasseRepository;

    public InfraClasseWrapper(InfraClasse infraClasse, InfraClasseQueryService infraClasseQueryService, InfraClasseRepository infraClasseRepository) {
        this.infraClasse = infraClasse;
        this.infraClasseQueryService = infraClasseQueryService;
        this.infraClasseRepository = infraClasseRepository;
    }

    @Override
    public CronquistRank getParent() {
        return infraClasse.getSousClasse();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return infraClasse.getSuperOrdres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (infraClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && infraClasse.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        InfraClasseCriteria infraClasseCrit = new InfraClasseCriteria();
        infraClasseCrit.setNomFr(getStringFilterEquals(infraClasse.getNomFr()));
        return infraClasseQueryService.findByCriteria(infraClasseCrit).stream().map(infraClasse1 -> new InfraClasseWrapper(infraClasse1, infraClasseQueryService, infraClasseRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return infraClasse.getId();
    }

    @Override
    public void setId(Long id) {
        infraClasse.setId(id);
    }

    @Override
    public String getNomFr() {
        return infraClasse.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        infraClasse.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return infraClasse;
    }

    @Override
    public void save() {
        infraClasseRepository.save(infraClasse);
    }
}
