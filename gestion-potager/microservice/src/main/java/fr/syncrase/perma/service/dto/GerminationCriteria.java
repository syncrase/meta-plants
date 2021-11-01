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
 * Criteria class for the {@link fr.syncrase.perma.domain.Germination} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.GerminationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /germinations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GerminationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tempsDeGermination;

    private StringFilter conditionDeGermination;

    public GerminationCriteria() {
    }

    public GerminationCriteria(GerminationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tempsDeGermination = other.tempsDeGermination == null ? null : other.tempsDeGermination.copy();
        this.conditionDeGermination = other.conditionDeGermination == null ? null : other.conditionDeGermination.copy();
    }

    @Override
    public GerminationCriteria copy() {
        return new GerminationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTempsDeGermination() {
        return tempsDeGermination;
    }

    public void setTempsDeGermination(StringFilter tempsDeGermination) {
        this.tempsDeGermination = tempsDeGermination;
    }

    public StringFilter getConditionDeGermination() {
        return conditionDeGermination;
    }

    public void setConditionDeGermination(StringFilter conditionDeGermination) {
        this.conditionDeGermination = conditionDeGermination;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GerminationCriteria that = (GerminationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tempsDeGermination, that.tempsDeGermination) &&
            Objects.equals(conditionDeGermination, that.conditionDeGermination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tempsDeGermination,
        conditionDeGermination
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GerminationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tempsDeGermination != null ? "tempsDeGermination=" + tempsDeGermination + ", " : "") +
                (conditionDeGermination != null ? "conditionDeGermination=" + conditionDeGermination + ", " : "") +
            "}";
    }

}
