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
 * Criteria class for the {@link fr.syncrase.perma.domain.Allelopathie} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.AllelopathieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /allelopathies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AllelopathieCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter description;

    private LongFilter cibleId;

    private LongFilter origineId;

    private LongFilter planteId;

    public AllelopathieCriteria() {
    }

    public AllelopathieCriteria(AllelopathieCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.cibleId = other.cibleId == null ? null : other.cibleId.copy();
        this.origineId = other.origineId == null ? null : other.origineId.copy();
        this.planteId = other.planteId == null ? null : other.planteId.copy();
    }

    @Override
    public AllelopathieCriteria copy() {
        return new AllelopathieCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getCibleId() {
        return cibleId;
    }

    public void setCibleId(LongFilter cibleId) {
        this.cibleId = cibleId;
    }

    public LongFilter getOrigineId() {
        return origineId;
    }

    public void setOrigineId(LongFilter origineId) {
        this.origineId = origineId;
    }

    public LongFilter getPlanteId() {
        return planteId;
    }

    public void setPlanteId(LongFilter planteId) {
        this.planteId = planteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AllelopathieCriteria that = (AllelopathieCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cibleId, that.cibleId) &&
            Objects.equals(origineId, that.origineId) &&
            Objects.equals(planteId, that.planteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        description,
        cibleId,
        origineId,
        planteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllelopathieCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (cibleId != null ? "cibleId=" + cibleId + ", " : "") +
                (origineId != null ? "origineId=" + origineId + ", " : "") +
                (planteId != null ? "planteId=" + planteId + ", " : "") +
            "}";
    }

}
