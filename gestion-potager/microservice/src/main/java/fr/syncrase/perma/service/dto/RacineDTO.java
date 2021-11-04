package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Racine} entity.
 */
public class RacineDTO implements Serializable {

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
        if (!(o instanceof RacineDTO)) {
            return false;
        }

        RacineDTO racineDTO = (RacineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, racineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RacineDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
