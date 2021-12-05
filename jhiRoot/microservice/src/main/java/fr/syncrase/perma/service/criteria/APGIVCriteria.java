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
 * Criteria class for the {@link fr.syncrase.perma.domain.APGIV} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.APGIVResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /apgivs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class APGIVCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ordre;

    private StringFilter famille;

    private Boolean distinct;

    public APGIVCriteria() {}

    public APGIVCriteria(APGIVCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ordre = other.ordre == null ? null : other.ordre.copy();
        this.famille = other.famille == null ? null : other.famille.copy();
        this.distinct = other.distinct;
    }

    @Override
    public APGIVCriteria copy() {
        return new APGIVCriteria(this);
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

    public StringFilter getOrdre() {
        return ordre;
    }

    public StringFilter ordre() {
        if (ordre == null) {
            ordre = new StringFilter();
        }
        return ordre;
    }

    public void setOrdre(StringFilter ordre) {
        this.ordre = ordre;
    }

    public StringFilter getFamille() {
        return famille;
    }

    public StringFilter famille() {
        if (famille == null) {
            famille = new StringFilter();
        }
        return famille;
    }

    public void setFamille(StringFilter famille) {
        this.famille = famille;
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
        final APGIVCriteria that = (APGIVCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(famille, that.famille) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ordre, famille, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGIVCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ordre != null ? "ordre=" + ordre + ", " : "") +
            (famille != null ? "famille=" + famille + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
