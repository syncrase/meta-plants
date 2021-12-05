package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Exposition} entity.
 */
public class ExpositionDTO implements Serializable {

    private Long id;

    private String valeur;

    private Double ensoleilement;

    private PlanteDTO plante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Double getEnsoleilement() {
        return ensoleilement;
    }

    public void setEnsoleilement(Double ensoleilement) {
        this.ensoleilement = ensoleilement;
    }

    public PlanteDTO getPlante() {
        return plante;
    }

    public void setPlante(PlanteDTO plante) {
        this.plante = plante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpositionDTO)) {
            return false;
        }

        ExpositionDTO expositionDTO = (ExpositionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, expositionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpositionDTO{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", ensoleilement=" + getEnsoleilement() +
            ", plante=" + getPlante() +
            "}";
    }
}
