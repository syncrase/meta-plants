package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.repository.EspeceRepository;
import fr.syncrase.ecosyst.service.EspeceQueryService;
import fr.syncrase.ecosyst.service.criteria.EspeceCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class EspeceWrapper implements CronquistRank {
    private final Espece espece;
    private final EspeceQueryService especeQueryService;
    private final EspeceRepository especeRepository;

    public EspeceWrapper(Espece espece, EspeceQueryService especeQueryService, EspeceRepository especeRepository) {
        this.espece = espece;
        this.especeQueryService = especeQueryService;
        this.especeRepository = especeRepository;
    }

    @Override
    public CronquistRank getParent() {
        return espece.getSousSection();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return espece.getSousEspeces();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (espece.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && espece.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        EspeceCriteria especeCrit = new EspeceCriteria();
        especeCrit.setNomFr(getStringFilterEquals(espece.getNomFr()));
        return especeQueryService.findByCriteria(especeCrit).stream().map(espece1 -> new EspeceWrapper(espece1, especeQueryService, especeRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return espece.getId();
    }

    @Override
    public void setId(Long id) {
        espece.setId(id);
    }

    @Override
    public String getNomFr() {
        return espece.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        espece.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return espece;
    }

    @Override
    public void save() {
        especeRepository.save(espece);
    }
}
