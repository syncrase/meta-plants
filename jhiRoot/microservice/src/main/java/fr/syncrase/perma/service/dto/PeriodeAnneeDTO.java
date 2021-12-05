package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.PeriodeAnnee} entity.
 */
public class PeriodeAnneeDTO implements Serializable {

    private Long id;

    private MoisDTO debut;

    private MoisDTO fin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MoisDTO getDebut() {
        return debut;
    }

    public void setDebut(MoisDTO debut) {
        this.debut = debut;
    }

    public MoisDTO getFin() {
        return fin;
    }

    public void setFin(MoisDTO fin) {
        this.fin = fin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodeAnneeDTO)) {
            return false;
        }

        PeriodeAnneeDTO periodeAnneeDTO = (PeriodeAnneeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, periodeAnneeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodeAnneeDTO{" +
            "id=" + getId() +
            ", debut=" + getDebut() +
            ", fin=" + getFin() +
            "}";
    }
}
