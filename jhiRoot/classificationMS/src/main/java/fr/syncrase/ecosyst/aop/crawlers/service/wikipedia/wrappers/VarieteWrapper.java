package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.VarieteRepository;
import fr.syncrase.ecosyst.service.VarieteQueryService;
import fr.syncrase.ecosyst.service.criteria.VarieteCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class VarieteWrapper implements CronquistRank {
    private final Variete variete;
    private final VarieteQueryService varieteQueryService;
    private final VarieteRepository varieteRepository;

    public VarieteWrapper(Variete variete, VarieteQueryService varieteQueryService, VarieteRepository varieteRepository) {
        this.variete = variete;
        this.varieteQueryService = varieteQueryService;
        this.varieteRepository = varieteRepository;
    }

    @Override
    public CronquistRank getParent() {
        return variete.getSousEspece();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return variete.getSousVarietes();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (variete.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && variete.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        VarieteCriteria varieteCrit = new VarieteCriteria();
        varieteCrit.setNomFr(getStringFilterEquals(variete.getNomFr()));
        return varieteQueryService.findByCriteria(varieteCrit).stream().map(variete1 -> new VarieteWrapper(variete1, varieteQueryService, varieteRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return variete.getId();
    }

    @Override
    public void setId(Long id) {
        variete.setId(id);
    }

    @Override
    public String getNomFr() {
        return variete.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        variete.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return variete;
    }

    @Override
    public void save() {
        varieteRepository.save(variete);
    }
}
