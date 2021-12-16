package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.wrappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistRank;
import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.repository.GenreRepository;
import fr.syncrase.ecosyst.service.GenreQueryService;
import fr.syncrase.ecosyst.service.criteria.GenreCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService.DEFAULT_NAME_FOR_CONNECTOR_RANK;

public class GenreWrapper implements CronquistRank {
    private final Genre genre;
    private final GenreQueryService genreQueryService;
    private final GenreRepository genreRepository;

    public GenreWrapper(Genre genre, GenreQueryService genreQueryService, GenreRepository genreRepository) {
        this.genre = genre;
        this.genreQueryService = genreQueryService;
        this.genreRepository = genreRepository;
    }

    @Override
    public CronquistRank getParent() {
        return genre.getSousTribu();
    }

    @Override
    public Set<? extends CronquistRank> getChildren() {
        return genre.getSousGenres();
    }

    @Override
    public List<? extends CronquistRank> findExistingRank() {
        if (genre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && genre.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        GenreCriteria genreCrit = new GenreCriteria();
        genreCrit.setNomFr(getStringFilterEquals(genre.getNomFr()));
        return genreQueryService.findByCriteria(genreCrit).stream().map(genre1 -> new GenreWrapper(genre1, genreQueryService, genreRepository)).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return genre.getId();
    }

    @Override
    public void setId(Long id) {
        genre.setId(id);
    }

    @Override
    public String getNomFr() {
        return genre.getNomFr();
    }

    @Override
    public void setNomFr(String nomFr) {
        genre.setNomFr(nomFr);
    }

    @Override
    public CronquistRank get() {
        return genre;
    }

    @Override
    public void save() {
        genreRepository.save(genre);
    }
}
