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
 * Criteria class for the {@link fr.syncrase.perma.domain.Ressemblance} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.RessemblanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ressemblances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RessemblanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LongFilter confusionId;

    public RessemblanceCriteria() {
    }

    public RessemblanceCriteria(RessemblanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.confusionId = other.confusionId == null ? null : other.confusionId.copy();
    }

    @Override
    public RessemblanceCriteria copy() {
        return new RessemblanceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getConfusionId() {
        return confusionId;
    }

    public void setConfusionId(LongFilter confusionId) {
        this.confusionId = confusionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RessemblanceCriteria that = (RessemblanceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(confusionId, that.confusionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        confusionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RessemblanceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (confusionId != null ? "confusionId=" + confusionId + ", " : "") +
            "}";
    }

}
