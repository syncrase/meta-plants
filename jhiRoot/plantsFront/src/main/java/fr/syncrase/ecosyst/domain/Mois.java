package fr.syncrase.ecosyst.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Mois.
 */
@Table("mois")
public class Mois implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("numero")
    private Double numero;

    @NotNull(message = "must not be null")
    @Column("nom")
    private String nom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mois id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNumero() {
        return this.numero;
    }

    public Mois numero(Double numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Double numero) {
        this.numero = numero;
    }

    public String getNom() {
        return this.nom;
    }

    public Mois nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mois)) {
            return false;
        }
        return id != null && id.equals(((Mois) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mois{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
