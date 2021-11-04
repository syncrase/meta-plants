package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Feuillage} entity.
 */
public class FeuillageDTO implements Serializable {

    private Long id;

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
        if (!(o instanceof FeuillageDTO)) {
            return false;
        }

        FeuillageDTO feuillageDTO = (FeuillageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feuillageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeuillageDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
