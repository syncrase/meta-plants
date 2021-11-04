package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Reproduction} entity.
 */
public class ReproductionDTO implements Serializable {

    private Long id;

    private String vitesse;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVitesse() {
        return vitesse;
    }

    public void setVitesse(String vitesse) {
        this.vitesse = vitesse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReproductionDTO)) {
            return false;
        }

        ReproductionDTO reproductionDTO = (ReproductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reproductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReproductionDTO{" +
            "id=" + getId() +
            ", vitesse='" + getVitesse() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
