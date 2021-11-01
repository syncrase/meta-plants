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
 * Criteria class for the {@link fr.syncrase.perma.domain.Semis} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.SemisResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /semis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SemisCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter semisPleineTerreId;

    private LongFilter semisSousAbrisId;

    private LongFilter typeSemisId;

    private LongFilter germinationId;

    public SemisCriteria() {
    }

    public SemisCriteria(SemisCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.semisPleineTerreId = other.semisPleineTerreId == null ? null : other.semisPleineTerreId.copy();
        this.semisSousAbrisId = other.semisSousAbrisId == null ? null : other.semisSousAbrisId.copy();
        this.typeSemisId = other.typeSemisId == null ? null : other.typeSemisId.copy();
        this.germinationId = other.germinationId == null ? null : other.germinationId.copy();
    }

    @Override
    public SemisCriteria copy() {
        return new SemisCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSemisPleineTerreId() {
        return semisPleineTerreId;
    }

    public void setSemisPleineTerreId(LongFilter semisPleineTerreId) {
        this.semisPleineTerreId = semisPleineTerreId;
    }

    public LongFilter getSemisSousAbrisId() {
        return semisSousAbrisId;
    }

    public void setSemisSousAbrisId(LongFilter semisSousAbrisId) {
        this.semisSousAbrisId = semisSousAbrisId;
    }

    public LongFilter getTypeSemisId() {
        return typeSemisId;
    }

    public void setTypeSemisId(LongFilter typeSemisId) {
        this.typeSemisId = typeSemisId;
    }

    public LongFilter getGerminationId() {
        return germinationId;
    }

    public void setGerminationId(LongFilter germinationId) {
        this.germinationId = germinationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SemisCriteria that = (SemisCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(semisPleineTerreId, that.semisPleineTerreId) &&
            Objects.equals(semisSousAbrisId, that.semisSousAbrisId) &&
            Objects.equals(typeSemisId, that.typeSemisId) &&
            Objects.equals(germinationId, that.germinationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        semisPleineTerreId,
        semisSousAbrisId,
        typeSemisId,
        germinationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemisCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (semisPleineTerreId != null ? "semisPleineTerreId=" + semisPleineTerreId + ", " : "") +
                (semisSousAbrisId != null ? "semisSousAbrisId=" + semisSousAbrisId + ", " : "") +
                (typeSemisId != null ? "typeSemisId=" + typeSemisId + ", " : "") +
                (germinationId != null ? "germinationId=" + germinationId + ", " : "") +
            "}";
    }

}
