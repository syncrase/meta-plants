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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SuperFamille} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SuperFamilleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /super-familles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SuperFamilleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter famillesId;

    private LongFilter synonymesId;

    private LongFilter microOrdreId;

    private LongFilter superFamilleId;

    private Boolean distinct;

    public SuperFamilleCriteria() {}

    public SuperFamilleCriteria(SuperFamilleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.famillesId = other.famillesId == null ? null : other.famillesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.microOrdreId = other.microOrdreId == null ? null : other.microOrdreId.copy();
        this.superFamilleId = other.superFamilleId == null ? null : other.superFamilleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SuperFamilleCriteria copy() {
        return new SuperFamilleCriteria(this);
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

    public StringFilter getNomFr() {
        return nomFr;
    }

    public StringFilter nomFr() {
        if (nomFr == null) {
            nomFr = new StringFilter();
        }
        return nomFr;
    }

    public void setNomFr(StringFilter nomFr) {
        this.nomFr = nomFr;
    }

    public StringFilter getNomLatin() {
        return nomLatin;
    }

    public StringFilter nomLatin() {
        if (nomLatin == null) {
            nomLatin = new StringFilter();
        }
        return nomLatin;
    }

    public void setNomLatin(StringFilter nomLatin) {
        this.nomLatin = nomLatin;
    }

    public LongFilter getFamillesId() {
        return famillesId;
    }

    public LongFilter famillesId() {
        if (famillesId == null) {
            famillesId = new LongFilter();
        }
        return famillesId;
    }

    public void setFamillesId(LongFilter famillesId) {
        this.famillesId = famillesId;
    }

    public LongFilter getSynonymesId() {
        return synonymesId;
    }

    public LongFilter synonymesId() {
        if (synonymesId == null) {
            synonymesId = new LongFilter();
        }
        return synonymesId;
    }

    public void setSynonymesId(LongFilter synonymesId) {
        this.synonymesId = synonymesId;
    }

    public LongFilter getMicroOrdreId() {
        return microOrdreId;
    }

    public LongFilter microOrdreId() {
        if (microOrdreId == null) {
            microOrdreId = new LongFilter();
        }
        return microOrdreId;
    }

    public void setMicroOrdreId(LongFilter microOrdreId) {
        this.microOrdreId = microOrdreId;
    }

    public LongFilter getSuperFamilleId() {
        return superFamilleId;
    }

    public LongFilter superFamilleId() {
        if (superFamilleId == null) {
            superFamilleId = new LongFilter();
        }
        return superFamilleId;
    }

    public void setSuperFamilleId(LongFilter superFamilleId) {
        this.superFamilleId = superFamilleId;
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
        final SuperFamilleCriteria that = (SuperFamilleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(famillesId, that.famillesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(microOrdreId, that.microOrdreId) &&
            Objects.equals(superFamilleId, that.superFamilleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, famillesId, synonymesId, microOrdreId, superFamilleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperFamilleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (famillesId != null ? "famillesId=" + famillesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (microOrdreId != null ? "microOrdreId=" + microOrdreId + ", " : "") +
            (superFamilleId != null ? "superFamilleId=" + superFamilleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
