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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousEspece} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousEspeceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-especes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousEspeceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter varietesId;

    private LongFilter synonymesId;

    private LongFilter especeId;

    private LongFilter sousEspeceId;

    private Boolean distinct;

    public SousEspeceCriteria() {}

    public SousEspeceCriteria(SousEspeceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.varietesId = other.varietesId == null ? null : other.varietesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.especeId = other.especeId == null ? null : other.especeId.copy();
        this.sousEspeceId = other.sousEspeceId == null ? null : other.sousEspeceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousEspeceCriteria copy() {
        return new SousEspeceCriteria(this);
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

    public LongFilter getVarietesId() {
        return varietesId;
    }

    public LongFilter varietesId() {
        if (varietesId == null) {
            varietesId = new LongFilter();
        }
        return varietesId;
    }

    public void setVarietesId(LongFilter varietesId) {
        this.varietesId = varietesId;
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

    public LongFilter getSousEspeceId() {
        return sousEspeceId;
    }

    public LongFilter sousEspeceId() {
        if (sousEspeceId == null) {
            sousEspeceId = new LongFilter();
        }
        return sousEspeceId;
    }

    public void setSousEspeceId(LongFilter sousEspeceId) {
        this.sousEspeceId = sousEspeceId;
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
        final SousEspeceCriteria that = (SousEspeceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(varietesId, that.varietesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(especeId, that.especeId) &&
            Objects.equals(sousEspeceId, that.sousEspeceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, varietesId, synonymesId, especeId, sousEspeceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousEspeceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (varietesId != null ? "varietesId=" + varietesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (especeId != null ? "especeId=" + especeId + ", " : "") +
            (sousEspeceId != null ? "sousEspeceId=" + sousEspeceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
