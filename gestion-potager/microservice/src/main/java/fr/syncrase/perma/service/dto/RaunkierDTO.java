package fr.syncrase.perma.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Raunkier} entity.
 */
public class RaunkierDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String type;

    
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RaunkierDTO)) {
            return false;
        }

        return id != null && id.equals(((RaunkierDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RaunkierDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
