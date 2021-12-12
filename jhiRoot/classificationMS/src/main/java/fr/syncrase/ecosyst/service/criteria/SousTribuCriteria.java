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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousTribu} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousTribuResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-tribus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousTribuCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter genresId;

    private LongFilter synonymesId;

    private LongFilter tribuId;

    private LongFilter sousTribuId;

    private Boolean distinct;

    public SousTribuCriteria() {}

    public SousTribuCriteria(SousTribuCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.genresId = other.genresId == null ? null : other.genresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.tribuId = other.tribuId == null ? null : other.tribuId.copy();
        this.sousTribuId = other.sousTribuId == null ? null : other.sousTribuId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousTribuCriteria copy() {
        return new SousTribuCriteria(this);
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

    public LongFilter getGenresId() {
        return genresId;
    }

    public LongFilter genresId() {
        if (genresId == null) {
            genresId = new LongFilter();
        }
        return genresId;
    }

    public void setGenresId(LongFilter genresId) {
        this.genresId = genresId;
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

    public LongFilter getTribuId() {
        return tribuId;
    }

    public LongFilter tribuId() {
        if (tribuId == null) {
            tribuId = new LongFilter();
        }
        return tribuId;
    }

    public void setTribuId(LongFilter tribuId) {
        this.tribuId = tribuId;
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
        final SousTribuCriteria that = (SousTribuCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(genresId, that.genresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(tribuId, that.tribuId) &&
            Objects.equals(sousTribuId, that.sousTribuId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, genresId, synonymesId, tribuId, sousTribuId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousTribuCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (genresId != null ? "genresId=" + genresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (tribuId != null ? "tribuId=" + tribuId + ", " : "") +
            (sousTribuId != null ? "sousTribuId=" + sousTribuId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
