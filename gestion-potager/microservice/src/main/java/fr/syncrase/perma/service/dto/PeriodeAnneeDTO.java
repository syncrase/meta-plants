package fr.syncrase.perma.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.PeriodeAnnee} entity.
 */
public class PeriodeAnneeDTO implements Serializable {
    
    private Long id;


    private Long debutId;

    private String debutNom;

    private Long finId;

    private String finNom;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDebutId() {
        return debutId;
    }

    public void setDebutId(Long moisId) {
        this.debutId = moisId;
    }

    public String getDebutNom() {
        return debutNom;
    }

    public void setDebutNom(String moisNom) {
        this.debutNom = moisNom;
    }

    public Long getFinId() {
        return finId;
    }

    public void setFinId(Long moisId) {
        this.finId = moisId;
    }

    public String getFinNom() {
        return finNom;
    }

    public void setFinNom(String moisNom) {
        this.finNom = moisNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodeAnneeDTO)) {
            return false;
        }

        return id != null && id.equals(((PeriodeAnneeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodeAnneeDTO{" +
            "id=" + getId() +
            ", debutId=" + getDebutId() +
            ", debutNom='" + getDebutNom() + "'" +
            ", finId=" + getFinId() +
            ", finNom='" + getFinNom() + "'" +
            "}";
    }
}
