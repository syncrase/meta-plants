package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Ordre;
import fr.syncrase.ecosyst.repository.OrdreRepository;
import fr.syncrase.ecosyst.service.OrdreQueryService;
import fr.syncrase.ecosyst.service.criteria.OrdreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class OrdreWrapper implements CronquistRank {
    private final Ordre ordre;
    private final OrdreQueryService ordreQueryService;
    private final OrdreRepository ordreRepository;

    public OrdreWrapper(Ordre ordre, OrdreQueryService ordreQueryService, OrdreRepository ordreRepository) {
        this.ordre = ordre;
        this.ordreQueryService = ordreQueryService;
        this.ordreRepository = ordreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return ordre.getSuperOrdre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return ordre.getSousOrdres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (ordre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && ordre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        OrdreCriteria ordreCrit = new OrdreCriteria();
        ordreCrit.setNomFr(getStringFilterEquals(ordre.getNomFr()));
        return ordreQueryService.findByCriteria(ordreCrit).stream().map(ordre1 -> new OrdreWrapper(ordre1, ordreQueryService, ordreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return ordre.getId();
    }

    @Override
    public void setId(Long id) {
        ordre.setId(id);
    }

    @Override
    public String getNomFr() {
        return ordre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        ordre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return ordre;
    }

    @Override
    public void save() {
        ordreRepository.save(ordre);
    }
}
