package fr.syncrase.ecosyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Un clade peut être référencé dans chacune des classifications phylogénétiques
 */
@ApiModel(description = "Un clade peut être référencé dans chacune des classifications phylogénétiques")
@Entity
@Table(name = "clade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Clade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToMany(mappedBy = "clades")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clades" }, allowSetters = true)
    private Set<APGIIIPlante> apgiiis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Clade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Clade nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<APGIIIPlante> getApgiiis() {
        return this.apgiiis;
    }

    public void setApgiiis(Set<APGIIIPlante> aPGIIIPlantes) {
        if (this.apgiiis != null) {
            this.apgiiis.forEach(i -> i.removeClades(this));
        }
        if (aPGIIIPlantes != null) {
            aPGIIIPlantes.forEach(i -> i.addClades(this));
        }
        this.apgiiis = aPGIIIPlantes;
    }

    public Clade apgiiis(Set<APGIIIPlante> aPGIIIPlantes) {
        this.setApgiiis(aPGIIIPlantes);
        return this;
    }

    public Clade addApgiiis(APGIIIPlante aPGIIIPlante) {
        this.apgiiis.add(aPGIIIPlante);
        aPGIIIPlante.getClades().add(this);
        return this;
    }

    public Clade removeApgiiis(APGIIIPlante aPGIIIPlante) {
        this.apgiiis.remove(aPGIIIPlante);
        aPGIIIPlante.getClades().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clade)) {
            return false;
        }
        return id != null && id.equals(((Clade) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clade{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
