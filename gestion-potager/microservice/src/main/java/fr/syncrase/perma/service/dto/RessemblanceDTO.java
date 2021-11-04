package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Ressemblance} entity.
 */
public class RessemblanceDTO implements Serializable {

    private Long id;

    private String description;

    private PlanteDTO confusion;

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

    public PlanteDTO getConfusion() {
        return confusion;
    }

    public void setConfusion(PlanteDTO confusion) {
        this.confusion = confusion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RessemblanceDTO)) {
            return false;
        }

        RessemblanceDTO ressemblanceDTO = (RessemblanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ressemblanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RessemblanceDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", confusion=" + getConfusion() +
            "}";
    }
}
