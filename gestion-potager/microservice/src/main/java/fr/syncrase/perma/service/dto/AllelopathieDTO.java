package fr.syncrase.perma.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Allelopathie} entity.
 */
public class AllelopathieDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String type;

    private String description;


    private Long cibleId;

    private Long origineId;

    private Long planteId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCibleId() {
        return cibleId;
    }

    public void setCibleId(Long planteId) {
        this.cibleId = planteId;
    }

    public Long getOrigineId() {
        return origineId;
    }

    public void setOrigineId(Long planteId) {
        this.origineId = planteId;
    }

    public Long getPlanteId() {
        return planteId;
    }

    public void setPlanteId(Long planteId) {
        this.planteId = planteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllelopathieDTO)) {
            return false;
        }

        return id != null && id.equals(((AllelopathieDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllelopathieDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", cibleId=" + getCibleId() +
            ", origineId=" + getOrigineId() +
            ", planteId=" + getPlanteId() +
            "}";
    }
}
