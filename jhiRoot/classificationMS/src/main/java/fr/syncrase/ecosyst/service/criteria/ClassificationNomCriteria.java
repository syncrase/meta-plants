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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.ClassificationNom} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.ClassificationNomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classification-noms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassificationNomCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter cronquistRankId;

    private Boolean distinct;

    public ClassificationNomCriteria() {}

    public ClassificationNomCriteria(ClassificationNomCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.cronquistRankId = other.cronquistRankId == null ? null : other.cronquistRankId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClassificationNomCriteria copy() {
        return new ClassificationNomCriteria(this);
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

    public LongFilter getCronquistRankId() {
        return cronquistRankId;
    }

    public LongFilter cronquistRankId() {
        if (cronquistRankId == null) {
            cronquistRankId = new LongFilter();
        }
        return cronquistRankId;
    }

    public void setCronquistRankId(LongFilter cronquistRankId) {
        this.cronquistRankId = cronquistRankId;
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
        final ClassificationNomCriteria that = (ClassificationNomCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(cronquistRankId, that.cronquistRankId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, cronquistRankId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationNomCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (cronquistRankId != null ? "cronquistRankId=" + cronquistRankId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
