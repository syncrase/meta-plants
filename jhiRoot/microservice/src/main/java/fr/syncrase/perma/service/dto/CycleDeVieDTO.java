package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.CycleDeVie} entity.
 */
public class CycleDeVieDTO implements Serializable {

    private Long id;

    private SemisDTO semis;

    private PeriodeAnneeDTO apparitionFeuilles;

    private PeriodeAnneeDTO floraison;

    private PeriodeAnneeDTO recolte;

    private PeriodeAnneeDTO croissance;

    private PeriodeAnneeDTO maturite;

    private PeriodeAnneeDTO plantation;

    private PeriodeAnneeDTO rempotage;

    private ReproductionDTO reproduction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SemisDTO getSemis() {
        return semis;
    }

    public void setSemis(SemisDTO semis) {
        this.semis = semis;
    }

    public PeriodeAnneeDTO getApparitionFeuilles() {
        return apparitionFeuilles;
    }

    public void setApparitionFeuilles(PeriodeAnneeDTO apparitionFeuilles) {
        this.apparitionFeuilles = apparitionFeuilles;
    }

    public PeriodeAnneeDTO getFloraison() {
        return floraison;
    }

    public void setFloraison(PeriodeAnneeDTO floraison) {
        this.floraison = floraison;
    }

    public PeriodeAnneeDTO getRecolte() {
        return recolte;
    }

    public void setRecolte(PeriodeAnneeDTO recolte) {
        this.recolte = recolte;
    }

    public PeriodeAnneeDTO getCroissance() {
        return croissance;
    }

    public void setCroissance(PeriodeAnneeDTO croissance) {
        this.croissance = croissance;
    }

    public PeriodeAnneeDTO getMaturite() {
        return maturite;
    }

    public void setMaturite(PeriodeAnneeDTO maturite) {
        this.maturite = maturite;
    }

    public PeriodeAnneeDTO getPlantation() {
        return plantation;
    }

    public void setPlantation(PeriodeAnneeDTO plantation) {
        this.plantation = plantation;
    }

    public PeriodeAnneeDTO getRempotage() {
        return rempotage;
    }

    public void setRempotage(PeriodeAnneeDTO rempotage) {
        this.rempotage = rempotage;
    }

    public ReproductionDTO getReproduction() {
        return reproduction;
    }

    public void setReproduction(ReproductionDTO reproduction) {
        this.reproduction = reproduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CycleDeVieDTO)) {
            return false;
        }

        CycleDeVieDTO cycleDeVieDTO = (CycleDeVieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cycleDeVieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDeVieDTO{" +
            "id=" + getId() +
            ", semis=" + getSemis() +
            ", apparitionFeuilles=" + getApparitionFeuilles() +
            ", floraison=" + getFloraison() +
            ", recolte=" + getRecolte() +
            ", croissance=" + getCroissance() +
            ", maturite=" + getMaturite() +
            ", plantation=" + getPlantation() +
            ", rempotage=" + getRempotage() +
            ", reproduction=" + getReproduction() +
            "}";
    }
}
