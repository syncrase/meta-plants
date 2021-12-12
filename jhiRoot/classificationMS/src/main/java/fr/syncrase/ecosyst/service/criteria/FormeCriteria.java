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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Forme} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.FormeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /formes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FormeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousFormesId;

    private LongFilter synonymesId;

    private LongFilter sousVarieteId;

    private LongFilter formeId;

    private Boolean distinct;

    public FormeCriteria() {}

    public FormeCriteria(FormeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousFormesId = other.sousFormesId == null ? null : other.sousFormesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousVarieteId = other.sousVarieteId == null ? null : other.sousVarieteId.copy();
        this.formeId = other.formeId == null ? null : other.formeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FormeCriteria copy() {
        return new FormeCriteria(this);
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

    public LongFilter getSousFormesId() {
        return sousFormesId;
    }

    public LongFilter sousFormesId() {
        if (sousFormesId == null) {
            sousFormesId = new LongFilter();
        }
        return sousFormesId;
    }

    public void setSousFormesId(LongFilter sousFormesId) {
        this.sousFormesId = sousFormesId;
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

    public LongFilter getSousVarieteId() {
        return sousVarieteId;
    }

    public LongFilter sousVarieteId() {
        if (sousVarieteId == null) {
            sousVarieteId = new LongFilter();
        }
        return sousVarieteId;
    }

    public void setSousVarieteId(LongFilter sousVarieteId) {
        this.sousVarieteId = sousVarieteId;
    }

    public LongFilter getFormeId() {
        return formeId;
    }

    public LongFilter formeId() {
        if (formeId == null) {
            formeId = new LongFilter();
        }
        return formeId;
    }

    public void setFormeId(LongFilter formeId) {
        this.formeId = formeId;
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
        final FormeCriteria that = (FormeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousFormesId, that.sousFormesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousVarieteId, that.sousVarieteId) &&
            Objects.equals(formeId, that.formeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousFormesId, synonymesId, sousVarieteId, formeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousFormesId != null ? "sousFormesId=" + sousFormesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousVarieteId != null ? "sousVarieteId=" + sousVarieteId + ", " : "") +
            (formeId != null ? "formeId=" + formeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
