package fr.syncrase.perma.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Plante} entity.
 */
public class PlanteDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String nomLatin;

    private String entretien;

    private String histoire;

    private String exposition;

    private String rusticite;


    private Long cycleDeVieId;

    private Long classificationId;
    private Set<NomVernaculaireDTO> nomsVernaculaires = new HashSet<>();
    
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

    public String getExposition() {
        return exposition;
    }

    public void setExposition(String exposition) {
        this.exposition = exposition;
    }

    public String getRusticite() {
        return rusticite;
    }

    public void setRusticite(String rusticite) {
        this.rusticite = rusticite;
    }

    public Long getCycleDeVieId() {
        return cycleDeVieId;
    }

    public void setCycleDeVieId(Long cycleDeVieId) {
        this.cycleDeVieId = cycleDeVieId;
    }

    public Long getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Long classificationId) {
        this.classificationId = classificationId;
    }

    public Set<NomVernaculaireDTO> getNomsVernaculaires() {
        return nomsVernaculaires;
    }

    public void setNomsVernaculaires(Set<NomVernaculaireDTO> nomVernaculaires) {
        this.nomsVernaculaires = nomVernaculaires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanteDTO)) {
            return false;
        }

        return id != null && id.equals(((PlanteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanteDTO{" +
            "id=" + getId() +
            ", nomLatin='" + getNomLatin() + "'" +
            ", entretien='" + getEntretien() + "'" +
            ", histoire='" + getHistoire() + "'" +
            ", exposition='" + getExposition() + "'" +
            ", rusticite='" + getRusticite() + "'" +
            ", cycleDeVieId=" + getCycleDeVieId() +
            ", classificationId=" + getClassificationId() +
            ", nomsVernaculaires='" + getNomsVernaculaires() + "'" +
            "}";
    }
}
