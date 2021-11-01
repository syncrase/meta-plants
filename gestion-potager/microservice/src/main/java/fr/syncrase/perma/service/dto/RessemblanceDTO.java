package fr.syncrase.perma.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Ressemblance} entity.
 */
public class RessemblanceDTO implements Serializable {
    
    private Long id;

    private String description;


    private Long confusionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getConfusionId() {
        return confusionId;
    }

    public void setConfusionId(Long planteId) {
        this.confusionId = planteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RessemblanceDTO)) {
            return false;
        }

        return id != null && id.equals(((RessemblanceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RessemblanceDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", confusionId=" + getConfusionId() +
            "}";
    }
}
