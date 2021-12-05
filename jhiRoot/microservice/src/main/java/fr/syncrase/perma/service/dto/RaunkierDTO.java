package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

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

        RaunkierDTO raunkierDTO = (RaunkierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, raunkierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
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
