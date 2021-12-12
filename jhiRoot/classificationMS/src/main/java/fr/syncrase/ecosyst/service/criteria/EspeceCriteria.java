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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Espece} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.EspeceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /especes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EspeceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousEspecesId;

    private LongFilter synonymesId;

    private LongFilter sousSectionId;

    private LongFilter especeId;

    private Boolean distinct;

    public EspeceCriteria() {}

    public EspeceCriteria(EspeceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousEspecesId = other.sousEspecesId == null ? null : other.sousEspecesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousSectionId = other.sousSectionId == null ? null : other.sousSectionId.copy();
        this.especeId = other.especeId == null ? null : other.especeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EspeceCriteria copy() {
        return new EspeceCriteria(this);
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

    public LongFilter getSousEspecesId() {
        return sousEspecesId;
    }

    public LongFilter sousEspecesId() {
        if (sousEspecesId == null) {
            sousEspecesId = new LongFilter();
        }
        return sousEspecesId;
    }

    public void setSousEspecesId(LongFilter sousEspecesId) {
        this.sousEspecesId = sousEspecesId;
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

    public LongFilter getEspeceId() {
        return especeId;
    }

    public LongFilter especeId() {
        if (especeId == null) {
            especeId = new LongFilter();
        }
        return especeId;
    }

    public void setEspeceId(LongFilter especeId) {
        this.especeId = especeId;
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
        final EspeceCriteria that = (EspeceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousEspecesId, that.sousEspecesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousSectionId, that.sousSectionId) &&
            Objects.equals(especeId, that.especeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousEspecesId, synonymesId, sousSectionId, especeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EspeceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousEspecesId != null ? "sousEspecesId=" + sousEspecesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousSectionId != null ? "sousSectionId=" + sousSectionId + ", " : "") +
            (especeId != null ? "especeId=" + especeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
