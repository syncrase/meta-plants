package fr.syncrase.perma.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Cronquist} entity.
 */
public class CronquistDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String regne;

    @NotNull
    private String sousRegne;

    @NotNull
    private String division;

    @NotNull
    private String classe;

    @NotNull
    private String sousClasse;

    @NotNull
    private String ordre;

    @NotNull
    private String famille;

    @NotNull
    private String genre;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegne() {
        return regne;
    }

    public void setRegne(String regne) {
        this.regne = regne;
    }

    public String getSousRegne() {
        return sousRegne;
    }

    public void setSousRegne(String sousRegne) {
        this.sousRegne = sousRegne;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getSousClasse() {
        return sousClasse;
    }

    public void setSousClasse(String sousClasse) {
        this.sousClasse = sousClasse;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronquistDTO)) {
            return false;
        }

        return id != null && id.equals(((CronquistDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronquistDTO{" +
            "id=" + getId() +
            ", regne='" + getRegne() + "'" +
            ", sousRegne='" + getSousRegne() + "'" +
            ", division='" + getDivision() + "'" +
            ", classe='" + getClasse() + "'" +
            ", sousClasse='" + getSousClasse() + "'" +
            ", ordre='" + getOrdre() + "'" +
            ", famille='" + getFamille() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
