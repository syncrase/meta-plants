package fr.syncrase.perma.service.dto;

import java.io.Serializable;

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

        return id != null && id.equals(((GerminationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
