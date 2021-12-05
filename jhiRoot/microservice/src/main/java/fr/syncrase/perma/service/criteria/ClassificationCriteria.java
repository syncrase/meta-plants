package fr.syncrase.perma.service.criteria;

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
 * Criteria class for the {@link fr.syncrase.perma.domain.Classification} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.ClassificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter raunkierId;

    private LongFilter cronquistId;

    private LongFilter apg1Id;

    private LongFilter apg2Id;

    private LongFilter apg3Id;

    private LongFilter apg4Id;

    private Boolean distinct;

    public ClassificationCriteria() {}

    public ClassificationCriteria(ClassificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.raunkierId = other.raunkierId == null ? null : other.raunkierId.copy();
        this.cronquistId = other.cronquistId == null ? null : other.cronquistId.copy();
        this.apg1Id = other.apg1Id == null ? null : other.apg1Id.copy();
        this.apg2Id = other.apg2Id == null ? null : other.apg2Id.copy();
        this.apg3Id = other.apg3Id == null ? null : other.apg3Id.copy();
        this.apg4Id = other.apg4Id == null ? null : other.apg4Id.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClassificationCriteria copy() {
        return new ClassificationCriteria(this);
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

    public LongFilter getRaunkierId() {
        return raunkierId;
    }

    public LongFilter raunkierId() {
        if (raunkierId == null) {
            raunkierId = new LongFilter();
        }
        return raunkierId;
    }

    public void setRaunkierId(LongFilter raunkierId) {
        this.raunkierId = raunkierId;
    }

    public LongFilter getCronquistId() {
        return cronquistId;
    }

    public LongFilter cronquistId() {
        if (cronquistId == null) {
            cronquistId = new LongFilter();
        }
        return cronquistId;
    }

    public void setCronquistId(LongFilter cronquistId) {
        this.cronquistId = cronquistId;
    }

    public LongFilter getApg1Id() {
        return apg1Id;
    }

    public LongFilter apg1Id() {
        if (apg1Id == null) {
            apg1Id = new LongFilter();
        }
        return apg1Id;
    }

    public void setApg1Id(LongFilter apg1Id) {
        this.apg1Id = apg1Id;
    }

    public LongFilter getApg2Id() {
        return apg2Id;
    }

    public LongFilter apg2Id() {
        if (apg2Id == null) {
            apg2Id = new LongFilter();
        }
        return apg2Id;
    }

    public void setApg2Id(LongFilter apg2Id) {
        this.apg2Id = apg2Id;
    }

    public LongFilter getApg3Id() {
        return apg3Id;
    }

    public LongFilter apg3Id() {
        if (apg3Id == null) {
            apg3Id = new LongFilter();
        }
        return apg3Id;
    }

    public void setApg3Id(LongFilter apg3Id) {
        this.apg3Id = apg3Id;
    }

    public LongFilter getApg4Id() {
        return apg4Id;
    }

    public LongFilter apg4Id() {
        if (apg4Id == null) {
            apg4Id = new LongFilter();
        }
        return apg4Id;
    }

    public void setApg4Id(LongFilter apg4Id) {
        this.apg4Id = apg4Id;
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
        final ClassificationCriteria that = (ClassificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(raunkierId, that.raunkierId) &&
            Objects.equals(cronquistId, that.cronquistId) &&
            Objects.equals(apg1Id, that.apg1Id) &&
            Objects.equals(apg2Id, that.apg2Id) &&
            Objects.equals(apg3Id, that.apg3Id) &&
            Objects.equals(apg4Id, that.apg4Id) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, raunkierId, cronquistId, apg1Id, apg2Id, apg3Id, apg4Id, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (raunkierId != null ? "raunkierId=" + raunkierId + ", " : "") +
            (cronquistId != null ? "cronquistId=" + cronquistId + ", " : "") +
            (apg1Id != null ? "apg1Id=" + apg1Id + ", " : "") +
            (apg2Id != null ? "apg2Id=" + apg2Id + ", " : "") +
            (apg3Id != null ? "apg3Id=" + apg3Id + ", " : "") +
            (apg4Id != null ? "apg4Id=" + apg4Id + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
