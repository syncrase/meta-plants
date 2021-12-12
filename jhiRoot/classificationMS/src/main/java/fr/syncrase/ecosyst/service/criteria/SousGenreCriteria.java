package fr.syncrase.ecosyst.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousGenre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousGenreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-genres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousGenreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sectionsId;

    private LongFilter synonymesId;

    private LongFilter genreId;

    private LongFilter sousGenreId;

    private Boolean distinct;

    public SousGenreCriteria() {}

    public SousGenreCriteria(SousGenreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sectionsId = other.sectionsId == null ? null : other.sectionsId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.genreId = other.genreId == null ? null : other.genreId.copy();
        this.sousGenreId = other.sousGenreId == null ? null : other.sousGenreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousGenreCriteria copy() {
        return new SousGenreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomFr() {
        return nomFr;
    }

    public StringFilter nomFr() {
        if (nomFr == null) {
            nomFr = new StringFilter();
        }
        return nomFr;
    }

    public void setNomFr(StringFilter nomFr) {
        this.nomFr = nomFr;
    }

    public StringFilter getNomLatin() {
        return nomLatin;
    }

    public StringFilter nomLatin() {
        if (nomLatin == null) {
            nomLatin = new StringFilter();
        }
        return nomLatin;
    }

    public void setNomLatin(StringFilter nomLatin) {
        this.nomLatin = nomLatin;
    }

    public LongFilter getSectionsId() {
        return sectionsId;
    }

    public LongFilter sectionsId() {
        if (sectionsId == null) {
            sectionsId = new LongFilter();
        }
        return sectionsId;
    }

    public void setSectionsId(LongFilter sectionsId) {
        this.sectionsId = sectionsId;
    }

    public LongFilter getSynonymesId() {
        return synonymesId;
    }

    public LongFilter synonymesId() {
        if (synonymesId == null) {
            synonymesId = new LongFilter();
        }
        return synonymesId;
    }

    public void setSynonymesId(LongFilter synonymesId) {
        this.synonymesId = synonymesId;
    }

    public LongFilter getGenreId() {
        return genreId;
    }

    public LongFilter genreId() {
        if (genreId == null) {
            genreId = new LongFilter();
        }
        return genreId;
    }

    public void setGenreId(LongFilter genreId) {
        this.genreId = genreId;
    }

    public LongFilter getSousGenreId() {
        return sousGenreId;
    }

    public LongFilter sousGenreId() {
        if (sousGenreId == null) {
            sousGenreId = new LongFilter();
        }
        return sousGenreId;
    }

    public void setSousGenreId(LongFilter sousGenreId) {
        this.sousGenreId = sousGenreId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SousGenreCriteria that = (SousGenreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sectionsId, that.sectionsId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(genreId, that.genreId) &&
            Objects.equals(sousGenreId, that.sousGenreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sectionsId, synonymesId, genreId, sousGenreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousGenreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sectionsId != null ? "sectionsId=" + sectionsId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (genreId != null ? "genreId=" + genreId + ", " : "") +
            (sousGenreId != null ? "sousGenreId=" + sousGenreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
