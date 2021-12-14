package fr.syncrase.ecosyst.service.criteria;

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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.APGIIIPlante} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.APGIIIPlanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /apgiii-plantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class APGIIIPlanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ordre;

    private StringFilter famille;

    private StringFilter sousFamille;

    private StringFilter tribu;

    private StringFilter sousTribu;

    private StringFilter genre;

    private LongFilter cladesId;

    private Boolean distinct;

    public APGIIIPlanteCriteria() {}

    public APGIIIPlanteCriteria(APGIIIPlanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ordre = other.ordre == null ? null : other.ordre.copy();
        this.famille = other.famille == null ? null : other.famille.copy();
        this.sousFamille = other.sousFamille == null ? null : other.sousFamille.copy();
        this.tribu = other.tribu == null ? null : other.tribu.copy();
        this.sousTribu = other.sousTribu == null ? null : other.sousTribu.copy();
        this.genre = other.genre == null ? null : other.genre.copy();
        this.cladesId = other.cladesId == null ? null : other.cladesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public APGIIIPlanteCriteria copy() {
        return new APGIIIPlanteCriteria(this);
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

    public StringFilter getSousFamille() {
        return sousFamille;
    }

    public StringFilter sousFamille() {
        if (sousFamille == null) {
            sousFamille = new StringFilter();
        }
        return sousFamille;
    }

    public void setSousFamille(StringFilter sousFamille) {
        this.sousFamille = sousFamille;
    }

    public StringFilter getTribu() {
        return tribu;
    }

    public StringFilter tribu() {
        if (tribu == null) {
            tribu = new StringFilter();
        }
        return tribu;
    }

    public void setTribu(StringFilter tribu) {
        this.tribu = tribu;
    }

    public StringFilter getSousTribu() {
        return sousTribu;
    }

    public StringFilter sousTribu() {
        if (sousTribu == null) {
            sousTribu = new StringFilter();
        }
        return sousTribu;
    }

    public void setSousTribu(StringFilter sousTribu) {
        this.sousTribu = sousTribu;
    }

    public StringFilter getGenre() {
        return genre;
    }

    public StringFilter genre() {
        if (genre == null) {
            genre = new StringFilter();
        }
        return genre;
    }

    public void setGenre(StringFilter genre) {
        this.genre = genre;
    }

    public LongFilter getCladesId() {
        return cladesId;
    }

    public LongFilter cladesId() {
        if (cladesId == null) {
            cladesId = new LongFilter();
        }
        return cladesId;
    }

    public void setCladesId(LongFilter cladesId) {
        this.cladesId = cladesId;
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
        final APGIIIPlanteCriteria that = (APGIIIPlanteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(famille, that.famille) &&
            Objects.equals(sousFamille, that.sousFamille) &&
            Objects.equals(tribu, that.tribu) &&
            Objects.equals(sousTribu, that.sousTribu) &&
            Objects.equals(genre, that.genre) &&
            Objects.equals(cladesId, that.cladesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ordre, famille, sousFamille, tribu, sousTribu, genre, cladesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGIIIPlanteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ordre != null ? "ordre=" + ordre + ", " : "") +
            (famille != null ? "famille=" + famille + ", " : "") +
            (sousFamille != null ? "sousFamille=" + sousFamille + ", " : "") +
            (tribu != null ? "tribu=" + tribu + ", " : "") +
            (sousTribu != null ? "sousTribu=" + sousTribu + ", " : "") +
            (genre != null ? "genre=" + genre + ", " : "") +
            (cladesId != null ? "cladesId=" + cladesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
