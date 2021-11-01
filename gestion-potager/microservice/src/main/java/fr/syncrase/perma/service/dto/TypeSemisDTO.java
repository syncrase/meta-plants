package fr.syncrase.perma.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.TypeSemis} entity.
 */
public class TypeSemisDTO implements Serializable {
    
    private Long id;

    private String description;

    
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSemisDTO)) {
            return false;
        }

        return id != null && id.equals(((TypeSemisDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSemisDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
