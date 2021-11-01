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
 * Criteria class for the {@link fr.syncrase.perma.domain.PeriodeAnnee} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.PeriodeAnneeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /periode-annees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PeriodeAnneeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter debutId;

    private LongFilter finId;

    public PeriodeAnneeCriteria() {
    }

    public PeriodeAnneeCriteria(PeriodeAnneeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.debutId = other.debutId == null ? null : other.debutId.copy();
        this.finId = other.finId == null ? null : other.finId.copy();
    }

    @Override
    public PeriodeAnneeCriteria copy() {
        return new PeriodeAnneeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getDebutId() {
        return debutId;
    }

    public void setDebutId(LongFilter debutId) {
        this.debutId = debutId;
    }

    public LongFilter getFinId() {
        return finId;
    }

    public void setFinId(LongFilter finId) {
        this.finId = finId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PeriodeAnneeCriteria that = (PeriodeAnneeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(debutId, that.debutId) &&
            Objects.equals(finId, that.finId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        debutId,
        finId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodeAnneeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (debutId != null ? "debutId=" + debutId + ", " : "") +
                (finId != null ? "finId=" + finId + ", " : "") +
            "}";
    }

}
