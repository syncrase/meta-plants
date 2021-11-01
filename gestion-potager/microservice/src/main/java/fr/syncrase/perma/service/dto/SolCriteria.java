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
 * Criteria class for the {@link fr.syncrase.perma.domain.Sol} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.SolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sols?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter acidite;

    private StringFilter type;

    public SolCriteria() {
    }

    public SolCriteria(SolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.acidite = other.acidite == null ? null : other.acidite.copy();
        this.type = other.type == null ? null : other.type.copy();
    }

    @Override
    public SolCriteria copy() {
        return new SolCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getAcidite() {
        return acidite;
    }

    public void setAcidite(DoubleFilter acidite) {
        this.acidite = acidite;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SolCriteria that = (SolCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(acidite, that.acidite) &&
            Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        acidite,
        type
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (acidite != null ? "acidite=" + acidite + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
            "}";
    }

}
