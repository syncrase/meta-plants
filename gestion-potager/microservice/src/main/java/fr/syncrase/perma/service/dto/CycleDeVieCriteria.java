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
 * Criteria class for the {@link fr.syncrase.perma.domain.CycleDeVie} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.CycleDeVieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cycle-de-vies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CycleDeVieCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter vitesseDeCroissance;

    private LongFilter semisId;

    private LongFilter apparitionFeuillesId;

    private LongFilter floraisonId;

    private LongFilter recolteId;

    private LongFilter croissanceId;

    private LongFilter maturiteId;

    private LongFilter plantationId;

    private LongFilter rempotageId;

    public CycleDeVieCriteria() {
    }

    public CycleDeVieCriteria(CycleDeVieCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.vitesseDeCroissance = other.vitesseDeCroissance == null ? null : other.vitesseDeCroissance.copy();
        this.semisId = other.semisId == null ? null : other.semisId.copy();
        this.apparitionFeuillesId = other.apparitionFeuillesId == null ? null : other.apparitionFeuillesId.copy();
        this.floraisonId = other.floraisonId == null ? null : other.floraisonId.copy();
        this.recolteId = other.recolteId == null ? null : other.recolteId.copy();
        this.croissanceId = other.croissanceId == null ? null : other.croissanceId.copy();
        this.maturiteId = other.maturiteId == null ? null : other.maturiteId.copy();
        this.plantationId = other.plantationId == null ? null : other.plantationId.copy();
        this.rempotageId = other.rempotageId == null ? null : other.rempotageId.copy();
    }

    @Override
    public CycleDeVieCriteria copy() {
        return new CycleDeVieCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVitesseDeCroissance() {
        return vitesseDeCroissance;
    }

    public void setVitesseDeCroissance(StringFilter vitesseDeCroissance) {
        this.vitesseDeCroissance = vitesseDeCroissance;
    }

    public LongFilter getSemisId() {
        return semisId;
    }

    public void setSemisId(LongFilter semisId) {
        this.semisId = semisId;
    }

    public LongFilter getApparitionFeuillesId() {
        return apparitionFeuillesId;
    }

    public void setApparitionFeuillesId(LongFilter apparitionFeuillesId) {
        this.apparitionFeuillesId = apparitionFeuillesId;
    }

    public LongFilter getFloraisonId() {
        return floraisonId;
    }

    public void setFloraisonId(LongFilter floraisonId) {
        this.floraisonId = floraisonId;
    }

    public LongFilter getRecolteId() {
        return recolteId;
    }

    public void setRecolteId(LongFilter recolteId) {
        this.recolteId = recolteId;
    }

    public LongFilter getCroissanceId() {
        return croissanceId;
    }

    public void setCroissanceId(LongFilter croissanceId) {
        this.croissanceId = croissanceId;
    }

    public LongFilter getMaturiteId() {
        return maturiteId;
    }

    public void setMaturiteId(LongFilter maturiteId) {
        this.maturiteId = maturiteId;
    }

    public LongFilter getPlantationId() {
        return plantationId;
    }

    public void setPlantationId(LongFilter plantationId) {
        this.plantationId = plantationId;
    }

    public LongFilter getRempotageId() {
        return rempotageId;
    }

    public void setRempotageId(LongFilter rempotageId) {
        this.rempotageId = rempotageId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CycleDeVieCriteria that = (CycleDeVieCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(vitesseDeCroissance, that.vitesseDeCroissance) &&
            Objects.equals(semisId, that.semisId) &&
            Objects.equals(apparitionFeuillesId, that.apparitionFeuillesId) &&
            Objects.equals(floraisonId, that.floraisonId) &&
            Objects.equals(recolteId, that.recolteId) &&
            Objects.equals(croissanceId, that.croissanceId) &&
            Objects.equals(maturiteId, that.maturiteId) &&
            Objects.equals(plantationId, that.plantationId) &&
            Objects.equals(rempotageId, that.rempotageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        vitesseDeCroissance,
        semisId,
        apparitionFeuillesId,
        floraisonId,
        recolteId,
        croissanceId,
        maturiteId,
        plantationId,
        rempotageId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDeVieCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (vitesseDeCroissance != null ? "vitesseDeCroissance=" + vitesseDeCroissance + ", " : "") +
                (semisId != null ? "semisId=" + semisId + ", " : "") +
                (apparitionFeuillesId != null ? "apparitionFeuillesId=" + apparitionFeuillesId + ", " : "") +
                (floraisonId != null ? "floraisonId=" + floraisonId + ", " : "") +
                (recolteId != null ? "recolteId=" + recolteId + ", " : "") +
                (croissanceId != null ? "croissanceId=" + croissanceId + ", " : "") +
                (maturiteId != null ? "maturiteId=" + maturiteId + ", " : "") +
                (plantationId != null ? "plantationId=" + plantationId + ", " : "") +
                (rempotageId != null ? "rempotageId=" + rempotageId + ", " : "") +
            "}";
    }

}
