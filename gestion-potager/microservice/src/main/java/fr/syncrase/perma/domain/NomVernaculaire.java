package fr.syncrase.perma.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A NomVernaculaire.
 */
@Entity
@Table(name = "nom_vernaculaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NomVernaculaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "nomsVernaculaires")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Plante> plantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public NomVernaculaire nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public NomVernaculaire description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Plante> getPlantes() {
        return plantes;
    }

    public NomVernaculaire plantes(Set<Plante> plantes) {
        this.plantes = plantes;
        return this;
    }

    public NomVernaculaire addPlantes(Plante plante) {
        this.plantes.add(plante);
        plante.getNomsVernaculaires().add(this);
        return this;
    }

    public NomVernaculaire removePlantes(Plante plante) {
        this.plantes.remove(plante);
        plante.getNomsVernaculaires().remove(this);
        return this;
    }

    public void setPlantes(Set<Plante> plantes) {
        this.plantes = plantes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NomVernaculaire)) {
            return false;
        }
        return id != null && id.equals(((NomVernaculaire) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NomVernaculaire{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
