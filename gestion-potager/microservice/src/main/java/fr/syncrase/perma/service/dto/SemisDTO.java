package fr.syncrase.perma.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Semis} entity.
 */
public class SemisDTO implements Serializable {
    
    private Long id;


    private Long semisPleineTerreId;

    private Long semisSousAbrisId;

    private Long typeSemisId;

    private Long germinationId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSemisPleineTerreId() {
        return semisPleineTerreId;
    }

    public void setSemisPleineTerreId(Long periodeAnneeId) {
        this.semisPleineTerreId = periodeAnneeId;
    }

    public Long getSemisSousAbrisId() {
        return semisSousAbrisId;
    }

    public void setSemisSousAbrisId(Long periodeAnneeId) {
        this.semisSousAbrisId = periodeAnneeId;
    }

    public Long getTypeSemisId() {
        return typeSemisId;
    }

    public void setTypeSemisId(Long typeSemisId) {
        this.typeSemisId = typeSemisId;
    }

    public Long getGerminationId() {
        return germinationId;
    }

    public void setGerminationId(Long germinationId) {
        this.germinationId = germinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemisDTO)) {
            return false;
        }

        return id != null && id.equals(((SemisDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemisDTO{" +
            "id=" + getId() +
            ", semisPleineTerreId=" + getSemisPleineTerreId() +
            ", semisSousAbrisId=" + getSemisSousAbrisId() +
            ", typeSemisId=" + getTypeSemisId() +
            ", germinationId=" + getGerminationId() +
            "}";
    }
}
