package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Allelopathie} entity.
 */
public class AllelopathieDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    private String description;

    private PlanteDTO cible;

    private PlanteDTO origine;

    private PlanteDTO interaction;

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

    public PlanteDTO getCible() {
        return cible;
    }

    public void setCible(PlanteDTO cible) {
        this.cible = cible;
    }

    public PlanteDTO getOrigine() {
        return origine;
    }

    public void setOrigine(PlanteDTO origine) {
        this.origine = origine;
    }

    public PlanteDTO getInteraction() {
        return interaction;
    }

    public void setInteraction(PlanteDTO interaction) {
        this.interaction = interaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllelopathieDTO)) {
            return false;
        }

        AllelopathieDTO allelopathieDTO = (AllelopathieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, allelopathieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllelopathieDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", cible=" + getCible() +
            ", origine=" + getOrigine() +
            ", interaction=" + getInteraction() +
            "}";
    }
}
