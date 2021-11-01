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
 * Criteria class for the {@link fr.syncrase.perma.domain.APGIII} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.APGIIIResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /apgiiis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class APGIIICriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ordre;

    private StringFilter famille;

    public APGIIICriteria() {
    }

    public APGIIICriteria(APGIIICriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ordre = other.ordre == null ? null : other.ordre.copy();
        this.famille = other.famille == null ? null : other.famille.copy();
    }

    @Override
    public APGIIICriteria copy() {
        return new APGIIICriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrdre() {
        return ordre;
    }

    public void setOrdre(StringFilter ordre) {
        this.ordre = ordre;
    }

    public StringFilter getFamille() {
        return famille;
    }

    public void setFamille(StringFilter famille) {
        this.famille = famille;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final APGIIICriteria that = (APGIIICriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(famille, that.famille);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ordre,
        famille
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGIIICriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ordre != null ? "ordre=" + ordre + ", " : "") +
                (famille != null ? "famille=" + famille + ", " : "") +
            "}";
    }

}
