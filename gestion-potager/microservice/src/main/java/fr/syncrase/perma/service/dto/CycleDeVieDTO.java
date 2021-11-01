package fr.syncrase.perma.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.CycleDeVie} entity.
 */
public class CycleDeVieDTO implements Serializable {
    
    private Long id;

    private String vitesseDeCroissance;


    private Long semisId;

    private Long apparitionFeuillesId;

    private Long floraisonId;

    private Long recolteId;

    private Long croissanceId;

    private Long maturiteId;

    private Long plantationId;

    private Long rempotageId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVitesseDeCroissance() {
        return vitesseDeCroissance;
    }

    public void setVitesseDeCroissance(String vitesseDeCroissance) {
        this.vitesseDeCroissance = vitesseDeCroissance;
    }

    public Long getSemisId() {
        return semisId;
    }

    public void setSemisId(Long semisId) {
        this.semisId = semisId;
    }

    public Long getApparitionFeuillesId() {
        return apparitionFeuillesId;
    }

    public void setApparitionFeuillesId(Long periodeAnneeId) {
        this.apparitionFeuillesId = periodeAnneeId;
    }

    public Long getFloraisonId() {
        return floraisonId;
    }

    public void setFloraisonId(Long periodeAnneeId) {
        this.floraisonId = periodeAnneeId;
    }

    public Long getRecolteId() {
        return recolteId;
    }

    public void setRecolteId(Long periodeAnneeId) {
        this.recolteId = periodeAnneeId;
    }

    public Long getCroissanceId() {
        return croissanceId;
    }

    public void setCroissanceId(Long periodeAnneeId) {
        this.croissanceId = periodeAnneeId;
    }

    public Long getMaturiteId() {
        return maturiteId;
    }

    public void setMaturiteId(Long periodeAnneeId) {
        this.maturiteId = periodeAnneeId;
    }

    public Long getPlantationId() {
        return plantationId;
    }

    public void setPlantationId(Long periodeAnneeId) {
        this.plantationId = periodeAnneeId;
    }

    public Long getRempotageId() {
        return rempotageId;
    }

    public void setRempotageId(Long periodeAnneeId) {
        this.rempotageId = periodeAnneeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CycleDeVieDTO)) {
            return false;
        }

        return id != null && id.equals(((CycleDeVieDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDeVieDTO{" +
            "id=" + getId() +
            ", vitesseDeCroissance='" + getVitesseDeCroissance() + "'" +
            ", semisId=" + getSemisId() +
            ", apparitionFeuillesId=" + getApparitionFeuillesId() +
            ", floraisonId=" + getFloraisonId() +
            ", recolteId=" + getRecolteId() +
            ", croissanceId=" + getCroissanceId() +
            ", maturiteId=" + getMaturiteId() +
            ", plantationId=" + getPlantationId() +
            ", rempotageId=" + getRempotageId() +
            "}";
    }
}
