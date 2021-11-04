package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Germination} entity.
 */
public class GerminationDTO implements Serializable {

    private Long id;

    private String tempsDeGermination;

    private String conditionDeGermination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTempsDeGermination() {
        return tempsDeGermination;
    }

    public void setTempsDeGermination(String tempsDeGermination) {
        this.tempsDeGermination = tempsDeGermination;
    }

    public String getConditionDeGermination() {
        return conditionDeGermination;
    }

    public void setConditionDeGermination(String conditionDeGermination) {
        this.conditionDeGermination = conditionDeGermination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GerminationDTO)) {
            return false;
        }

        GerminationDTO germinationDTO = (GerminationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, germinationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GerminationDTO{" +
            "id=" + getId() +
            ", tempsDeGermination='" + getTempsDeGermination() + "'" +
            ", conditionDeGermination='" + getConditionDeGermination() + "'" +
            "}";
    }
}
