package fr.syncrase.perma.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Ressemblance} entity.
 */
@ApiModel(
    description = "Pour que la ressemblance soit réflexive il faut l'enregistrer 2 fois. Car si la ressemblance A ressemble à B est enregistrée, alors B ne ressemble pas à A"
)
public class RessemblanceDTO implements Serializable {

    private Long id;

    private String description;

    private PlanteDTO planteRessemblant;

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

    public PlanteDTO getPlanteRessemblant() {
        return planteRessemblant;
    }

    public void setPlanteRessemblant(PlanteDTO planteRessemblant) {
        this.planteRessemblant = planteRessemblant;
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
            ", planteRessemblant=" + getPlanteRessemblant() +
            "}";
    }
}
