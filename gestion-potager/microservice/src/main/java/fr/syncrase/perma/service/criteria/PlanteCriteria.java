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
 * Criteria class for the {@link fr.syncrase.perma.domain.Plante} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.PlanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomLatin;

    private StringFilter entretien;

    private StringFilter histoire;

    private StringFilter vitesse;

    private LongFilter cycleDeVieId;

    private LongFilter classificationId;

    private LongFilter confusionsId;

    private LongFilter interactionsId;

    private LongFilter expositionsId;

    private LongFilter solsId;

    private LongFilter nomsVernaculairesId;

    private LongFilter temperatureId;

    private LongFilter racineId;

    private LongFilter strateId;

    private LongFilter feuillageId;

    private Boolean distinct;

    public PlanteCriteria() {}

    public PlanteCriteria(PlanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.entretien = other.entretien == null ? null : other.entretien.copy();
        this.histoire = other.histoire == null ? null : other.histoire.copy();
        this.vitesse = other.vitesse == null ? null : other.vitesse.copy();
        this.cycleDeVieId = other.cycleDeVieId == null ? null : other.cycleDeVieId.copy();
        this.classificationId = other.classificationId == null ? null : other.classificationId.copy();
        this.confusionsId = other.confusionsId == null ? null : other.confusionsId.copy();
        this.interactionsId = other.interactionsId == null ? null : other.interactionsId.copy();
        this.expositionsId = other.expositionsId == null ? null : other.expositionsId.copy();
        this.solsId = other.solsId == null ? null : other.solsId.copy();
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

    public StringFilter getVitesse() {
        return vitesse;
    }

    public StringFilter vitesse() {
        if (vitesse == null) {
            vitesse = new StringFilter();
        }
        return vitesse;
    }

    public void setVitesse(StringFilter vitesse) {
        this.vitesse = vitesse;
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

    public LongFilter getInteractionsId() {
        return interactionsId;
    }

    public LongFilter interactionsId() {
        if (interactionsId == null) {
            interactionsId = new LongFilter();
        }
        return interactionsId;
    }

    public void setInteractionsId(LongFilter interactionsId) {
        this.interactionsId = interactionsId;
    }

    public LongFilter getExpositionsId() {
        return expositionsId;
    }

    public LongFilter expositionsId() {
        if (expositionsId == null) {
            expositionsId = new LongFilter();
        }
        return expositionsId;
    }

    public void setExpositionsId(LongFilter expositionsId) {
        this.expositionsId = expositionsId;
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
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(entretien, that.entretien) &&
            Objects.equals(histoire, that.histoire) &&
            Objects.equals(vitesse, that.vitesse) &&
            Objects.equals(cycleDeVieId, that.cycleDeVieId) &&
            Objects.equals(classificationId, that.classificationId) &&
            Objects.equals(confusionsId, that.confusionsId) &&
            Objects.equals(interactionsId, that.interactionsId) &&
            Objects.equals(expositionsId, that.expositionsId) &&
            Objects.equals(solsId, that.solsId) &&
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
            nomLatin,
            entretien,
            histoire,
            vitesse,
            cycleDeVieId,
            classificationId,
            confusionsId,
            interactionsId,
            expositionsId,
            solsId,
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
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (entretien != null ? "entretien=" + entretien + ", " : "") +
            (histoire != null ? "histoire=" + histoire + ", " : "") +
            (vitesse != null ? "vitesse=" + vitesse + ", " : "") +
            (cycleDeVieId != null ? "cycleDeVieId=" + cycleDeVieId + ", " : "") +
            (classificationId != null ? "classificationId=" + classificationId + ", " : "") +
            (confusionsId != null ? "confusionsId=" + confusionsId + ", " : "") +
            (interactionsId != null ? "interactionsId=" + interactionsId + ", " : "") +
            (expositionsId != null ? "expositionsId=" + expositionsId + ", " : "") +
            (solsId != null ? "solsId=" + solsId + ", " : "") +
            (nomsVernaculairesId != null ? "nomsVernaculairesId=" + nomsVernaculairesId + ", " : "") +
            (temperatureId != null ? "temperatureId=" + temperatureId + ", " : "") +
            (racineId != null ? "racineId=" + racineId + ", " : "") +
            (strateId != null ? "strateId=" + strateId + ", " : "") +
            (feuillageId != null ? "feuillageId=" + feuillageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
