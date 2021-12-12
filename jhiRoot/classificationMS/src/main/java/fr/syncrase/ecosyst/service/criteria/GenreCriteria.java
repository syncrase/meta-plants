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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Genre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.GenreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /genres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GenreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousGenresId;

    private LongFilter synonymesId;

    private LongFilter sousTribuId;

    private LongFilter genreId;

    private Boolean distinct;

    public GenreCriteria() {}

    public GenreCriteria(GenreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousGenresId = other.sousGenresId == null ? null : other.sousGenresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousTribuId = other.sousTribuId == null ? null : other.sousTribuId.copy();
        this.genreId = other.genreId == null ? null : other.genreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GenreCriteria copy() {
        return new GenreCriteria(this);
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

    public LongFilter getSousGenresId() {
        return sousGenresId;
    }

    public LongFilter sousGenresId() {
        if (sousGenresId == null) {
            sousGenresId = new LongFilter();
        }
        return sousGenresId;
    }

    public void setSousGenresId(LongFilter sousGenresId) {
        this.sousGenresId = sousGenresId;
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

    public LongFilter getSousTribuId() {
        return sousTribuId;
    }

    public LongFilter sousTribuId() {
        if (sousTribuId == null) {
            sousTribuId = new LongFilter();
        }
        return sousTribuId;
    }

    public void setSousTribuId(LongFilter sousTribuId) {
        this.sousTribuId = sousTribuId;
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
        final GenreCriteria that = (GenreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousGenresId, that.sousGenresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousTribuId, that.sousTribuId) &&
            Objects.equals(genreId, that.genreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousGenresId, synonymesId, sousTribuId, genreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousGenresId != null ? "sousGenresId=" + sousGenresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousTribuId != null ? "sousTribuId=" + sousTribuId + ", " : "") +
            (genreId != null ? "genreId=" + genreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
