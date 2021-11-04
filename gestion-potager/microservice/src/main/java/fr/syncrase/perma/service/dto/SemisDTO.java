package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Semis} entity.
 */
public class SemisDTO implements Serializable {

    private Long id;

    private PeriodeAnneeDTO semisPleineTerre;

    private PeriodeAnneeDTO semisSousAbris;

    private TypeSemisDTO typeSemis;

    private GerminationDTO germination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeriodeAnneeDTO getSemisPleineTerre() {
        return semisPleineTerre;
    }

    public void setSemisPleineTerre(PeriodeAnneeDTO semisPleineTerre) {
        this.semisPleineTerre = semisPleineTerre;
    }

    public PeriodeAnneeDTO getSemisSousAbris() {
        return semisSousAbris;
    }

    public void setSemisSousAbris(PeriodeAnneeDTO semisSousAbris) {
        this.semisSousAbris = semisSousAbris;
    }

    public TypeSemisDTO getTypeSemis() {
        return typeSemis;
    }

    public void setTypeSemis(TypeSemisDTO typeSemis) {
        this.typeSemis = typeSemis;
    }

    public GerminationDTO getGermination() {
        return germination;
    }

    public void setGermination(GerminationDTO germination) {
        this.germination = germination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemisDTO)) {
            return false;
        }

        SemisDTO semisDTO = (SemisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, semisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemisDTO{" +
            "id=" + getId() +
            ", semisPleineTerre=" + getSemisPleineTerre() +
            ", semisSousAbris=" + getSemisSousAbris() +
            ", typeSemis=" + getTypeSemis() +
            ", germination=" + getGermination() +
            "}";
    }
}
