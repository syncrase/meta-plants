package fr.syncrase.perma.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.APGIII} entity.
 */
public class APGIIIDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String ordre;

    @NotNull
    private String famille;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof APGIIIDTO)) {
            return false;
        }

        return id != null && id.equals(((APGIIIDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGIIIDTO{" +
            "id=" + getId() +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            "}";
    }
}
