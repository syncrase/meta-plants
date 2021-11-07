package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Ensoleillement} entity.
 */
public class EnsoleillementDTO implements Serializable {

    private Long id;

    private String orientation;

    private Double ensoleilement;

    private PlanteDTO plante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
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
        if (!(o instanceof EnsoleillementDTO)) {
            return false;
        }

        EnsoleillementDTO ensoleillementDTO = (EnsoleillementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ensoleillementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnsoleillementDTO{" +
            "id=" + getId() +
            ", orientation='" + getOrientation() + "'" +
            ", ensoleilement=" + getEnsoleilement() +
            ", plante=" + getPlante() +
            "}";
    }
}
