package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Plante} entity.
 */
public class PlanteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomLatin;

    private String entretien;

    private String histoire;

    private String vitesseCroissance;

    private String exposition;

    private CycleDeVieDTO cycleDeVie;

    private ClassificationDTO classification;

    private Set<NomVernaculaireDTO> nomsVernaculaires = new HashSet<>();

    private TemperatureDTO temperature;

    private RacineDTO racine;

    private StrateDTO strate;

    private FeuillageDTO feuillage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLatin() {
        return nomLatin;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public String getEntretien() {
        return entretien;
    }

    public void setEntretien(String entretien) {
        this.entretien = entretien;
    }

    public String getHistoire() {
        return histoire;
    }

    public void setHistoire(String histoire) {
        this.histoire = histoire;
    }

    public String getVitesseCroissance() {
        return vitesseCroissance;
    }

    public void setVitesseCroissance(String vitesseCroissance) {
        this.vitesseCroissance = vitesseCroissance;
    }

    public String getExposition() {
        return exposition;
    }

    public void setExposition(String exposition) {
        this.exposition = exposition;
    }

    public CycleDeVieDTO getCycleDeVie() {
        return cycleDeVie;
    }

    public void setCycleDeVie(CycleDeVieDTO cycleDeVie) {
        this.cycleDeVie = cycleDeVie;
    }

    public ClassificationDTO getClassification() {
        return classification;
    }

    public void setClassification(ClassificationDTO classification) {
        this.classification = classification;
    }

    public Set<NomVernaculaireDTO> getNomsVernaculaires() {
        return nomsVernaculaires;
    }

    public void setNomsVernaculaires(Set<NomVernaculaireDTO> nomsVernaculaires) {
        this.nomsVernaculaires = nomsVernaculaires;
    }

    public TemperatureDTO getTemperature() {
        return temperature;
    }

    public void setTemperature(TemperatureDTO temperature) {
        this.temperature = temperature;
    }

    public RacineDTO getRacine() {
        return racine;
    }

    public void setRacine(RacineDTO racine) {
        this.racine = racine;
    }

    public StrateDTO getStrate() {
        return strate;
    }

    public void setStrate(StrateDTO strate) {
        this.strate = strate;
    }

    public FeuillageDTO getFeuillage() {
        return feuillage;
    }

    public void setFeuillage(FeuillageDTO feuillage) {
        this.feuillage = feuillage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanteDTO)) {
            return false;
        }

        PlanteDTO planteDTO = (PlanteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanteDTO{" +
            "id=" + getId() +
            ", nomLatin='" + getNomLatin() + "'" +
            ", entretien='" + getEntretien() + "'" +
            ", histoire='" + getHistoire() + "'" +
            ", vitesseCroissance='" + getVitesseCroissance() + "'" +
            ", exposition='" + getExposition() + "'" +
            ", cycleDeVie=" + getCycleDeVie() +
            ", classification=" + getClassification() +
            ", nomsVernaculaires=" + getNomsVernaculaires() +
            ", temperature=" + getTemperature() +
            ", racine=" + getRacine() +
            ", strate=" + getStrate() +
            ", feuillage=" + getFeuillage() +
            "}";
    }
}
