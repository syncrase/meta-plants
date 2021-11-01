package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

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

    public ClassificationCriteria() {
    }

    public ClassificationCriteria(ClassificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.raunkierId = other.raunkierId == null ? null : other.raunkierId.copy();
        this.cronquistId = other.cronquistId == null ? null : other.cronquistId.copy();
        this.apg1Id = other.apg1Id == null ? null : other.apg1Id.copy();
        this.apg2Id = other.apg2Id == null ? null : other.apg2Id.copy();
        this.apg3Id = other.apg3Id == null ? null : other.apg3Id.copy();
        this.apg4Id = other.apg4Id == null ? null : other.apg4Id.copy();
    }

    @Override
    public ClassificationCriteria copy() {
        return new ClassificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getRaunkierId() {
        return raunkierId;
    }

    public void setRaunkierId(LongFilter raunkierId) {
        this.raunkierId = raunkierId;
    }

    public LongFilter getCronquistId() {
        return cronquistId;
    }

    public void setCronquistId(LongFilter cronquistId) {
        this.cronquistId = cronquistId;
    }

    public LongFilter getApg1Id() {
        return apg1Id;
    }

    public void setApg1Id(LongFilter apg1Id) {
        this.apg1Id = apg1Id;
    }

    public LongFilter getApg2Id() {
        return apg2Id;
    }

    public void setApg2Id(LongFilter apg2Id) {
        this.apg2Id = apg2Id;
    }

    public LongFilter getApg3Id() {
        return apg3Id;
    }

    public void setApg3Id(LongFilter apg3Id) {
        this.apg3Id = apg3Id;
    }

    public LongFilter getApg4Id() {
        return apg4Id;
    }

    public void setApg4Id(LongFilter apg4Id) {
        this.apg4Id = apg4Id;
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
        return
            Objects.equals(id, that.id) &&
            Objects.equals(raunkierId, that.raunkierId) &&
            Objects.equals(cronquistId, that.cronquistId) &&
            Objects.equals(apg1Id, that.apg1Id) &&
            Objects.equals(apg2Id, that.apg2Id) &&
            Objects.equals(apg3Id, that.apg3Id) &&
            Objects.equals(apg4Id, that.apg4Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        raunkierId,
        cronquistId,
        apg1Id,
        apg2Id,
        apg3Id,
        apg4Id
        );
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
            "}";
    }

}
