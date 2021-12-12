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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousSection} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousSectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-sections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousSectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter especesId;

    private LongFilter synonymesId;

    private LongFilter sectionId;

    private LongFilter sousSectionId;

    private Boolean distinct;

    public SousSectionCriteria() {}

    public SousSectionCriteria(SousSectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.especesId = other.especesId == null ? null : other.especesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sectionId = other.sectionId == null ? null : other.sectionId.copy();
        this.sousSectionId = other.sousSectionId == null ? null : other.sousSectionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousSectionCriteria copy() {
        return new SousSectionCriteria(this);
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

    public LongFilter getEspecesId() {
        return especesId;
    }

    public LongFilter especesId() {
        if (especesId == null) {
            especesId = new LongFilter();
        }
        return especesId;
    }

    public void setEspecesId(LongFilter especesId) {
        this.especesId = especesId;
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

    public LongFilter getSousSectionId() {
        return sousSectionId;
    }

    public LongFilter sousSectionId() {
        if (sousSectionId == null) {
            sousSectionId = new LongFilter();
        }
        return sousSectionId;
    }

    public void setSousSectionId(LongFilter sousSectionId) {
        this.sousSectionId = sousSectionId;
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
        final SousSectionCriteria that = (SousSectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(especesId, that.especesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sectionId, that.sectionId) &&
            Objects.equals(sousSectionId, that.sousSectionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, especesId, synonymesId, sectionId, sousSectionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousSectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (especesId != null ? "especesId=" + especesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sectionId != null ? "sectionId=" + sectionId + ", " : "") +
            (sousSectionId != null ? "sousSectionId=" + sousSectionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
