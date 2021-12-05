package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.APGI} entity.
 */
public class APGIDTO implements Serializable {

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
        if (!(o instanceof APGIDTO)) {
            return false;
        }

        APGIDTO aPGIDTO = (APGIDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aPGIDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APGIDTO{" +
            "id=" + getId() +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            "}";
    }
}
