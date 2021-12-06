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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Plante} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.PlanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter entretien;

    private StringFilter histoire;

    private StringFilter vitesseCroissance;

    private StringFilter exposition;

    private LongFilter cycleDeVieId;

    private LongFilter confusionsId;

    private LongFilter ensoleillementsId;

    private LongFilter solsId;

    private LongFilter classificationId;

    private LongFilter nomsVernaculairesId;

    private LongFilter temperatureId;

    private LongFilter racineId;

    private LongFilter strateId;

    private LongFilter feuillageId;

    private Boolean distinct;

    public PlanteCriteria() {}

    public PlanteCriteria(PlanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entretien = other.entretien == null ? null : other.entretien.copy();
        this.histoire = other.histoire == null ? null : other.histoire.copy();
        this.vitesseCroissance = other.vitesseCroissance == null ? null : other.vitesseCroissance.copy();
        this.exposition = other.exposition == null ? null : other.exposition.copy();
        this.cycleDeVieId = other.cycleDeVieId == null ? null : other.cycleDeVieId.copy();
        this.confusionsId = other.confusionsId == null ? null : other.confusionsId.copy();
        this.ensoleillementsId = other.ensoleillementsId == null ? null : other.ensoleillementsId.copy();
        this.solsId = other.solsId == null ? null : other.solsId.copy();
        this.classificationId = other.classificationId == null ? null : other.classificationId.copy();
        this.nomsVernaculairesId = other.nomsVernaculairesId == null ? null : other.nomsVernaculairesId.copy();
        this.temperatureId = other.temperatureId == null ? null : other.temperatureId.copy();
        this.racineId = other.racineId == null ? null : other.racineId.copy();
        this.strateId = other.strateId == null ? null : other.strateId.copy();
        this.feuillageId = other.feuillageId == null ? null : other.feuillageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PlanteCriteria copy() {
        return new PlanteCriteria(this);
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

    public StringFilter getEntretien() {
        return entretien;
    }

    public StringFilter entretien() {
        if (entretien == null) {
            entretien = new StringFilter();
        }
        return entretien;
    }

    public void setEntretien(StringFilter entretien) {
        this.entretien = entretien;
    }

    public StringFilter getHistoire() {
        return histoire;
    }

    public StringFilter histoire() {
        if (histoire == null) {
            histoire = new StringFilter();
        }
        return histoire;
    }

    public void setHistoire(StringFilter histoire) {
        this.histoire = histoire;
    }

    public StringFilter getVitesseCroissance() {
        return vitesseCroissance;
    }

    public StringFilter vitesseCroissance() {
        if (vitesseCroissance == null) {
            vitesseCroissance = new StringFilter();
        }
        return vitesseCroissance;
    }

    public void setVitesseCroissance(StringFilter vitesseCroissance) {
        this.vitesseCroissance = vitesseCroissance;
    }

    public StringFilter getExposition() {
        return exposition;
    }

    public StringFilter exposition() {
        if (exposition == null) {
            exposition = new StringFilter();
        }
        return exposition;
    }

    public void setExposition(StringFilter exposition) {
        this.exposition = exposition;
    }

    public LongFilter getCycleDeVieId() {
        return cycleDeVieId;
    }

    public LongFilter cycleDeVieId() {
        if (cycleDeVieId == null) {
            cycleDeVieId = new LongFilter();
        }
        return cycleDeVieId;
    }

    public void setCycleDeVieId(LongFilter cycleDeVieId) {
        this.cycleDeVieId = cycleDeVieId;
    }

    public LongFilter getConfusionsId() {
        return confusionsId;
    }

    public LongFilter confusionsId() {
        if (confusionsId == null) {
            confusionsId = new LongFilter();
        }
        return confusionsId;
    }

    public void setConfusionsId(LongFilter confusionsId) {
        this.confusionsId = confusionsId;
    }

    public LongFilter getEnsoleillementsId() {
        return ensoleillementsId;
    }

    public LongFilter ensoleillementsId() {
        if (ensoleillementsId == null) {
            ensoleillementsId = new LongFilter();
        }
        return ensoleillementsId;
    }

    public void setEnsoleillementsId(LongFilter ensoleillementsId) {
        this.ensoleillementsId = ensoleillementsId;
    }

    public LongFilter getSolsId() {
        return solsId;
    }

    public LongFilter solsId() {
        if (solsId == null) {
            solsId = new LongFilter();
        }
        return solsId;
    }

    public void setSolsId(LongFilter solsId) {
        this.solsId = solsId;
    }

    public LongFilter getClassificationId() {
        return classificationId;
    }

    public LongFilter classificationId() {
        if (classificationId == null) {
            classificationId = new LongFilter();
        }
        return classificationId;
    }

    public void setClassificationId(LongFilter classificationId) {
        this.classificationId = classificationId;
    }

    public LongFilter getNomsVernaculairesId() {
        return nomsVernaculairesId;
    }

    public LongFilter nomsVernaculairesId() {
        if (nomsVernaculairesId == null) {
            nomsVernaculairesId = new LongFilter();
        }
        return nomsVernaculairesId;
    }

    public void setNomsVernaculairesId(LongFilter nomsVernaculairesId) {
        this.nomsVernaculairesId = nomsVernaculairesId;
    }

    public LongFilter getTemperatureId() {
        return temperatureId;
    }

    public LongFilter temperatureId() {
        if (temperatureId == null) {
            temperatureId = new LongFilter();
        }
        return temperatureId;
    }

    public void setTemperatureId(LongFilter temperatureId) {
        this.temperatureId = temperatureId;
    }

    public LongFilter getRacineId() {
        return racineId;
    }

    public LongFilter racineId() {
        if (racineId == null) {
            racineId = new LongFilter();
        }
        return racineId;
    }

    public void setRacineId(LongFilter racineId) {
        this.racineId = racineId;
    }

    public LongFilter getStrateId() {
        return strateId;
    }

    public LongFilter strateId() {
        if (strateId == null) {
            strateId = new LongFilter();
        }
        return strateId;
    }

    public void setStrateId(LongFilter strateId) {
        this.strateId = strateId;
    }

    public LongFilter getFeuillageId() {
        return feuillageId;
    }

    public LongFilter feuillageId() {
        if (feuillageId == null) {
            feuillageId = new LongFilter();
        }
        return feuillageId;
    }

    public void setFeuillageId(LongFilter feuillageId) {
        this.feuillageId = feuillageId;
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
        final PlanteCriteria that = (PlanteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entretien, that.entretien) &&
            Objects.equals(histoire, that.histoire) &&
            Objects.equals(vitesseCroissance, that.vitesseCroissance) &&
            Objects.equals(exposition, that.exposition) &&
            Objects.equals(cycleDeVieId, that.cycleDeVieId) &&
            Objects.equals(confusionsId, that.confusionsId) &&
            Objects.equals(ensoleillementsId, that.ensoleillementsId) &&
            Objects.equals(solsId, that.solsId) &&
            Objects.equals(classificationId, that.classificationId) &&
            Objects.equals(nomsVernaculairesId, that.nomsVernaculairesId) &&
            Objects.equals(temperatureId, that.temperatureId) &&
            Objects.equals(racineId, that.racineId) &&
            Objects.equals(strateId, that.strateId) &&
            Objects.equals(feuillageId, that.feuillageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            entretien,
            histoire,
            vitesseCroissance,
            exposition,
            cycleDeVieId,
            confusionsId,
            ensoleillementsId,
            solsId,
            classificationId,
            nomsVernaculairesId,
            temperatureId,
            racineId,
            strateId,
            feuillageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entretien != null ? "entretien=" + entretien + ", " : "") +
            (histoire != null ? "histoire=" + histoire + ", " : "") +
            (vitesseCroissance != null ? "vitesseCroissance=" + vitesseCroissance + ", " : "") +
            (exposition != null ? "exposition=" + exposition + ", " : "") +
            (cycleDeVieId != null ? "cycleDeVieId=" + cycleDeVieId + ", " : "") +
            (confusionsId != null ? "confusionsId=" + confusionsId + ", " : "") +
            (ensoleillementsId != null ? "ensoleillementsId=" + ensoleillementsId + ", " : "") +
            (solsId != null ? "solsId=" + solsId + ", " : "") +
            (classificationId != null ? "classificationId=" + classificationId + ", " : "") +
            (nomsVernaculairesId != null ? "nomsVernaculairesId=" + nomsVernaculairesId + ", " : "") +
            (temperatureId != null ? "temperatureId=" + temperatureId + ", " : "") +
            (racineId != null ? "racineId=" + racineId + ", " : "") +
            (strateId != null ? "strateId=" + strateId + ", " : "") +
            (feuillageId != null ? "feuillageId=" + feuillageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}