package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.repository.SousGenreRepository;
import fr.syncrase.ecosyst.service.SousGenreQueryService;
import fr.syncrase.ecosyst.service.criteria.SousGenreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class SousGenreWrapper implements CronquistRank {
    private final SousGenre sousGenre;
    private final SousGenreQueryService sousGenreQueryService;
    private final SousGenreRepository sousGenreRepository;

    public SousGenreWrapper(SousGenre sousGenre, SousGenreQueryService sousGenreQueryService, SousGenreRepository sousGenreRepository) {
        this.sousGenre = sousGenre;
        this.sousGenreQueryService = sousGenreQueryService;
        this.sousGenreRepository = sousGenreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return sousGenre.getGenre();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return sousGenre.getSections();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (sousGenre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && sousGenre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        SousGenreCriteria sousGenreCrit = new SousGenreCriteria();
        sousGenreCrit.setNomFr(getStringFilterEquals(sousGenre.getNomFr()));
        return sousGenreQueryService.findByCriteria(sousGenreCrit).stream().map(sousGenre1 -> new SousGenreWrapper(sousGenre1, sousGenreQueryService, sousGenreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return sousGenre.getId();
    }

    @Override
    public void setId(Long id) {
        sousGenre.setId(id);
    }

    @Override
    public String getNomFr() {
        return sousGenre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        sousGenre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return sousGenre;
    }

    @Override
    public void save() {
        sousGenreRepository.save(sousGenre);
    }
}
