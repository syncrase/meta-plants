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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Section} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousSectionsId;

    private LongFilter synonymesId;

    private LongFilter sousGenreId;

    private LongFilter sectionId;

    private Boolean distinct;

    public SectionCriteria() {}

    public SectionCriteria(SectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousSectionsId = other.sousSectionsId == null ? null : other.sousSectionsId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousGenreId = other.sousGenreId == null ? null : other.sousGenreId.copy();
        this.sectionId = other.sectionId == null ? null : other.sectionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SectionCriteria copy() {
        return new SectionCriteria(this);
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

    public LongFilter getSousSectionsId() {
        return sousSectionsId;
    }

    public LongFilter sousSectionsId() {
        if (sousSectionsId == null) {
            sousSectionsId = new LongFilter();
        }
        return sousSectionsId;
    }

    public void setSousSectionsId(LongFilter sousSectionsId) {
        this.sousSectionsId = sousSectionsId;
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

    public LongFilter getSectionId() {
        return sectionId;
    }

    public LongFilter sectionId() {
        if (sectionId == null) {
            sectionId = new LongFilter();
        }
        return sectionId;
    }

    public void setSectionId(LongFilter sectionId) {
        this.sectionId = sectionId;
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
        final SectionCriteria that = (SectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousSectionsId, that.sousSectionsId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousGenreId, that.sousGenreId) &&
            Objects.equals(sectionId, that.sectionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousSectionsId, synonymesId, sousGenreId, sectionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousSectionsId != null ? "sousSectionsId=" + sousSectionsId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousGenreId != null ? "sousGenreId=" + sousGenreId + ", " : "") +
            (sectionId != null ? "sectionId=" + sectionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
