package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Sol} entity.
 */
public class SolDTO implements Serializable {

    private Long id;

    private Double phMin;

    private Double phMax;

    private String type;

    private String richesse;

    private PlanteDTO plante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPhMin() {
        return phMin;
    }

    public void setPhMin(Double phMin) {
        this.phMin = phMin;
    }

    public Double getPhMax() {
        return phMax;
    }

    public void setPhMax(Double phMax) {
        this.phMax = phMax;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRichesse() {
        return richesse;
    }

    public void setRichesse(String richesse) {
        this.richesse = richesse;
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
        if (!(o instanceof SolDTO)) {
            return false;
        }

        SolDTO solDTO = (SolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, solDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolDTO{" +
            "id=" + getId() +
            ", phMin=" + getPhMin() +
            ", phMax=" + getPhMax() +
            ", type='" + getType() + "'" +
            ", richesse='" + getRichesse() + "'" +
            ", plante=" + getPlante() +
            "}";
    }
}
