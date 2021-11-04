package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.syncrase.perma.domain.Mois} entity.
 */
public class MoisDTO implements Serializable {

    private Long id;

    @NotNull
    private Double numero;

    @NotNull
    private String nom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNumero() {
        return numero;
    }

    public void setNumero(Double numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoisDTO)) {
            return false;
        }

        MoisDTO moisDTO = (MoisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoisDTO{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
